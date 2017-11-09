package webSessions;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Created by Oscar Reyes on 4/21/2017.
 * Class for setting up Capabilities for PhantomJS.
 */
public final class PhantomJsSetter {
    public static WebDriver getWebDriver() {
        // Set-up PhantomJS
        Capabilities caps = setUpCapabilities();
        // Initialize WebDriver
        return new PhantomJSDriver(caps);
    }

    /**
     * @return returns Capabilities object, indicating the desired capabilities for PhantomJS.
     */
    private static Capabilities setUpCapabilities() {
        Capabilities caps = new DesiredCapabilities();
        ((DesiredCapabilities) caps).setJavascriptEnabled(true);
        ((DesiredCapabilities) caps).setCapability("takesScreenshot", true);
        ((DesiredCapabilities) caps).setCapability(
                PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                "C:\\PhantomJs\\bin\\phantomjs.exe"
        );
        return caps;
    }
}
