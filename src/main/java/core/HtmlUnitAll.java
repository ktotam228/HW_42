package core;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class HtmlUnitAll {
	private static String myLinks(String [] setOfLinks){
		String result = "";
		for(int i=0;i<setOfLinks.length;i++){
			Logger.getLogger("").setLevel(Level.OFF);
			String driverPath = "";
			if (System.getProperty("os.name").toUpperCase().contains("MAC")) driverPath = "./resources/webdrivers/mac/geckodriver.sh";
			else if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) driverPath = "./resources/webdrivers/pc/geckodriver.exe";
			else throw new IllegalArgumentException("Unknown OS");
			System.setProperty("webdriver.gecko.driver", driverPath);
			WebDriver driver = new HtmlUnitDriver();
			String browser = "HtmlUnit";
			((HtmlUnitDriver) driver).setJavascriptEnabled(true);
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			driver.manage().window().maximize();
			driver.get(setOfLinks[i]);
			String string_monthly_payment = driver.findElement(By.id("id_monthly_payment")).getText();

//			String regex = "^(\$)?(\s)?\d+([,\.])?\d+\.?\d+(\$)?$";
//			!!!!!!! This RegEx works but Java doesn't accept it !!!!!!!!!!!!
			String regex = "^(?:\\$)?(?:\\s*)?((?:\\d{1,3})(?:\\,)?(?:\\d{3})?(?:\\.)?(\\d{0,2})?)$";
			

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
			result = "Browser: \t"+browser+";\n"+"String: \t"+string_monthly_payment+";\nAnnual Payment: "+f_annual_payment+
					";\nResult: \t"+actual_result+";\n____________________\n";
			driver.quit();
			System.out.printf(result);
		}
		return result;
	}

	public static void main(String[] args) {
		String [] linksSet = {"http://alex.academy/exe/payment/index.html",
				"http://alex.academy/exe/payment/index2.html",
				"http://alex.academy/exe/payment/index3.html",
				"http://alex.academy/exe/payment/index4.html",
				"http://alex.academy/exe/payment/indexE.html"};
		HtmlUnitAll myInstance = new HtmlUnitAll();
		myInstance.myLinks(linksSet);

	}

}