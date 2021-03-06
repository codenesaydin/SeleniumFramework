package base;

import driver.DriverManager;;
import driver.DriverWebTestFactory;
import net.lightbody.bmp.core.har.Har;
import org.junit.*;
import org.junit.rules.TestName;
import properties.LoadProperties;
import recorder.VideoRecorder;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import static properties.LoadProperties.LoadConfigProperty;
import static properties.SetProperties.SetValueProperties;

public abstract class BaseResponsiveTest extends AbstractSeleniumTest {

    private final static Logger LOGGER = Logger.getLogger(BaseResponsiveTest.class.getName());

    private VideoRecorder videoRecorder;

    @Override
    protected void createDriver() {
    }

    @Rule
    public final TestName testName = new TestName();

    @Before
    public void init() throws Exception {

        SetValueProperties();
        LoadConfigProperty();

        DriverManager driverManager;
        driverManager = DriverWebTestFactory.getManager();
        driver = driverManager.getDriver();

        VideoRecorder.startRecording(testName.getMethodName());

    }


    @After
    public void tearDown() throws Exception {

        setHarFile(testName.getMethodName());
        VideoRecorder.stopRecording();

        if (driver != null) {
            driver.close();
            driver.quit();
        }

    }


    // --------

    private void setHarFile(String harFileName) {

        String sFileName = LoadProperties.config.getProperty("HarFilePath") + harFileName + ".har";

        try {
            Har har = proxy.getHar();
            File harFile = new File(sFileName);
            try {
                har.writeTo(harFile);
            } catch (IOException ex) {
                System.out.println(ex.toString());
                System.out.println("Could not find file " + sFileName);
            }
        } catch (Exception ex) {
        }
    }

}
