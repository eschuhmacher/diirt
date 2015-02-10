/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.javafx.tools;

import java.net.URL;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import org.diirt.datasource.PV;
import org.diirt.datasource.PVManager;
import org.diirt.datasource.PVReaderEvent;
import org.diirt.datasource.PVWriterEvent;
import org.diirt.datasource.formula.ExpressionLanguage;
import org.diirt.javafx.util.Executors;
import org.diirt.util.time.TimeDuration;
import org.diirt.vtype.Alarm;
import org.diirt.vtype.AlarmSeverity;
import org.diirt.vtype.SimpleValueFormat;
import org.diirt.vtype.ValueFormat;
import org.diirt.vtype.ValueUtil;

public class ProbeController implements Initializable {
    
    private PV<?, Object> pv;
    
    private ValueFormat format = new SimpleValueFormat(3);
    
    @FXML
    private TextField channelField;
    @FXML
    private TextField valueField;
    @FXML
    private TextField newValueField;
    @FXML
    private TextField errorField;
    @FXML
    private ValueViewer valueViewer;
    @FXML
    private EventLogViewer eventLogViewer;

    @FXML
    private void onChannelChanged(ActionEvent event) {
        if (pv != null) {
            pv.close();
            newValueField.setText(null);
            valueField.setText(null);
            newValueField.setEditable(false);
            newValueField.setDisable(true);
        }

        pv = PVManager.readAndWrite(ExpressionLanguage.formula(channelField.getText()))
                .readListener(eventLogViewer.eventLog().<Object>createReadListener())
                .readListener((PVReaderEvent<Object> e) -> {
                    valueField.setText(format.format(e.getPvReader().getValue()));
                    setAlarm(e.getPvReader().getValue());
                    valueViewer.setValue(e.getPvReader().getValue(), e.getPvReader().isConnected());
                    Event lastEvent = eventLogViewer.eventLog().getEvents().get(eventLogViewer.eventLog().getEvents().size() - 1);
                    if (lastEvent instanceof ReadEvent) {
                        Exception lastException = ((ReadEvent) lastEvent).getLastException();
                        if (lastException != null) {
                            errorField.setText(lastException.getMessage());
                        } else {
                            errorField.setText(null);
                        }
                    }
                })
                .writeListener(eventLogViewer.eventLog().<Object>createWriteListener(channelField.getText()))
                .writeListener((PVWriterEvent<Object> e) -> {
                    if (e.isConnectionChanged()) {
                        if (e.getPvWriter().isWriteConnected()) {
                            newValueField.setDisable(false);
                            newValueField.setEditable(true);
                        }
                    }
                })
                .notifyOn(Executors.javaFXAT())
                .asynchWriteAndMaxReadRate(TimeDuration.ofHertz(50));
    }
    
    private static final Map<AlarmSeverity, Border> BORDER_MAP = createBorderMap();

    private static Map<AlarmSeverity, Border> createBorderMap() {
        Map<AlarmSeverity, Border> map = new EnumMap<>(AlarmSeverity.class);
        map.put(AlarmSeverity.NONE, null);
        map.put(AlarmSeverity.MINOR, new Border(new BorderStroke(Color.YELLOW, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        map.put(AlarmSeverity.MAJOR, new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        map.put(AlarmSeverity.INVALID, new Border(new BorderStroke(Color.PURPLE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        map.put(AlarmSeverity.UNDEFINED, new Border(new BorderStroke(Color.PURPLE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        return Collections.unmodifiableMap(map);
    }
    
    private void setAlarm(Object value) {
        Alarm alarm = ValueUtil.alarmOf(value, pv.isConnected());
        valueField.setBorder(BORDER_MAP.get(alarm.getAlarmSeverity()));
    }

    @FXML
    private void onNewValueChanged(ActionEvent event) {
        pv.write(newValueField.getText());
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
