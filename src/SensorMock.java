public class SensorMock
        implements MonitoravelViaIoT {

    @Override
    public void transmitirDadosSensor() {

        System.out.println(
                "Mock enviando dados do sensor."
        );
    }
}