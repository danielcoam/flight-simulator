package server;

import commands.ConnectCommand;
import interpreter.MyInterpreter;

import java.io.*;
import java.util.Timer;
import java.util.TimerTask;

public class MyClientHandler implements ClientHandler {

    private class SimDataGetter extends TimerTask {
        private String inputFromClient;

        @Override
        public void run() {
            if (inputFromClient != null) {
                try {
                    String[] str = inputFromClient.split(",");
                    if (ConnectCommand.isSimulatorReady()) {
                        MyInterpreter.getSymbolTable().updateFromSimulator("/instrumentation/airspeed-indicator/indicated-speed-kt", Double.parseDouble(str[0]));
                        MyInterpreter.getSymbolTable().updateFromSimulator("/instrumentation/altimeter/indicated-altitude-ft", Double.parseDouble(str[1]));
                        MyInterpreter.getSymbolTable().updateFromSimulator("/instrumentation/altimeter/pressure-alt-ft", Double.parseDouble(str[2]));
                        MyInterpreter.getSymbolTable().updateFromSimulator("/instrumentation/attitude-indicator/indicated-pitch-deg", Double.parseDouble(str[3]));
                        MyInterpreter.getSymbolTable().updateFromSimulator("/instrumentation/attitude-indicator/indicated-roll-deg", Double.parseDouble(str[4]));
                        MyInterpreter.getSymbolTable().updateFromSimulator("/instrumentation/attitude-indicator/internal-pitch-deg", Double.parseDouble(str[5]));
                        MyInterpreter.getSymbolTable().updateFromSimulator("/instrumentation/attitude-indicator/internal-roll-deg", Double.parseDouble(str[6]));
                        MyInterpreter.getSymbolTable().updateFromSimulator("/instrumentation/encoder/indicated-altitude-ft", Double.parseDouble(str[7]));
                        MyInterpreter.getSymbolTable().updateFromSimulator("/instrumentation/encoder/pressure-alt-ft", Double.parseDouble(str[8]));
                        MyInterpreter.getSymbolTable().updateFromSimulator("/instrumentation/gps/indicated-altitude-ft", Double.parseDouble(str[9]));
                        MyInterpreter.getSymbolTable().updateFromSimulator("/instrumentation/gps/indicated-ground-speed-kt", Double.parseDouble(str[10]));
                        MyInterpreter.getSymbolTable().updateFromSimulator("/instrumentation/gps/indicated-vertical-speed", Double.parseDouble(str[11]));
                        MyInterpreter.getSymbolTable().updateFromSimulator("/instrumentation/heading-indicator/indicated-heading-deg", Double.parseDouble(str[12]));
                        MyInterpreter.getSymbolTable().updateFromSimulator("/instrumentation/magnetic-compass/indicated-heading-deg", Double.parseDouble(str[13]));
                        MyInterpreter.getSymbolTable().updateFromSimulator("/instrumentation/slip-skid-ball/indicated-slip-skid", Double.parseDouble(str[14]));
                        MyInterpreter.getSymbolTable().updateFromSimulator("/instrumentation/turn-indicator/indicated-turn-rate", Double.parseDouble(str[15]));
                        MyInterpreter.getSymbolTable().updateFromSimulator("/instrumentation/vertical-speed-indicator/indicated-speed-fpm", Double.parseDouble(str[16]));
                        MyInterpreter.getSymbolTable().updateFromSimulator("/controls/flight/aileron", Double.parseDouble(str[17]));
                        MyInterpreter.getSymbolTable().updateFromSimulator("/controls/flight/elevator", Double.parseDouble(str[18]));
                        MyInterpreter.getSymbolTable().updateFromSimulator("/controls/flight/rudder", Double.parseDouble(str[19]));
                        MyInterpreter.getSymbolTable().updateFromSimulator("/controls/flight/flaps", Double.parseDouble(str[20]));
                        MyInterpreter.getSymbolTable().updateFromSimulator("/controls/engines/current-engine/throttle", Double.parseDouble(str[21]));
                        MyInterpreter.getSymbolTable().updateFromSimulator("/engines/engine/rpm", Double.parseDouble(str[22]));
                    } else {
                        if (Double.parseDouble(str[22]) != 0) {
                            System.out.println("simulator is ready");
                            ConnectCommand.setSimulatorCondition(true);
                        }
                    }
                } catch (NullPointerException e) {
                } catch (NumberFormatException e) {
                }
            }
        }
    }

    private int Hz;
    private SimDataGetter dataGetter;

    public MyClientHandler(int Hz) {
        this.Hz = Hz;
        dataGetter = new SimDataGetter();
    }

    @Override
    public void handleClient(InputStream in, OutputStream out) throws IOException {
        BufferedReader clientInput = new BufferedReader(new InputStreamReader(in));
        int frequency = 1000 / Hz;
        Timer t = new Timer();
        t.scheduleAtFixedRate(dataGetter, 0, frequency);
        while ((dataGetter.inputFromClient = clientInput.readLine()) != null)
            ;
        dataGetter.cancel();
        t.cancel();
    }

}
