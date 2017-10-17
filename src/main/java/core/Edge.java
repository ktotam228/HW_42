package core;

import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import java.math.*;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;
import java.util.regex.*;

public class Edge {

	public static void main(String[] args) throws InterruptedException {
		Logger.getLogger("").setLevel(Level.OFF);
		String url = "http://alex.academy/exe/payment/index.html";
		
		if (!System.getProperty("os.name").contains("WINDOWS")) throw new IllegalArgumentException("Edge is not available only on Mac");
		
		System.setProperty("webdriver.edge.driver", "./resources/webdrivers/pc/MicrosoftWebDriver.exe");
		WebDriver driver = new EdgeDriver();
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		driver.manage().window().maximize();

		driver.get(url);

		String string_monthly_payment = driver.findElement(By.id("id_monthly_payment")).getText();

		// String regex = "^"    // ^ Start og line
		// + "(?:\\$)?"                // ?: Passive Group (non-capturing) & \? Liteal $ & ? 0 or 1
		// + "(?:\\s*)?"               // ?: Passive Group (non-capturing) & \s - Whitespace characters
		// + "((?:\\d{1,3})(?:\\,)?(?:\\d{3})?(?:\\.)?(\\d{0,2})?)"
		// + "$";                         // $ End of Line

		String regex = "(1,654.55)";

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(string_monthly_payment);
		m.find();
		double monthly_payment = Double.parseDouble(m.group(1).replaceAll(",", ""));
		double annual_payment = new BigDecimal(monthly_payment * 12).setScale(2, RoundingMode.HALF_UP).doubleValue();
		DecimalFormat df = new DecimalFormat("0.00");
		String f_annual_payment = df.format(annual_payment);
		driver.findElement(By.id("id_annual_payment")).sendKeys(String.valueOf(f_annual_payment));
		driver.findElement(By.id("id_validate_button")).submit();
		String actual_result = driver.findElement(By.id("id_result")).getText();
		System.out.println("String: \t" + string_monthly_payment);
		System.out.println("Annual Payment: " + f_annual_payment);
		System.out.println("Result: \t" + actual_result);
		driver.quit();
	}
}
