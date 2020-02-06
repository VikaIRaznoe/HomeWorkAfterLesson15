package tests;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;


import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class SampleTest {
    private AppiumDriver<MobileElement> driver;

    @BeforeTest
    public void setUp() throws MalformedURLException{
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(MobileCapabilityType.PLATFORM_NAME,"Android");
        caps.setCapability(MobileCapabilityType.PLATFORM_VERSION,"8.0");
        caps.setCapability(MobileCapabilityType.DEVICE_NAME,"emulator-5554");
        caps.setCapability("appPackage","com.afollestad.materialdialogssample");
        caps.setCapability("appActivity","com.afollestad.materialdialogssample.MainActivity");
        caps.setCapability("app","/Users/viktoria/IdeaProjects/Java/ExampleIvanAlfa/src/test/resources/Apps/sample.apk");

        caps.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT,"80");

        URL appiumURL = new URL("http://127.0.0.1:4723/wd/hub");

        driver = new AppiumDriver<MobileElement>(appiumURL,caps);

    }

    @AfterTest
    public void tearDown(){
        driver.quit();
    }

    //Метод ожидания появления элемента
    private WebElement waitForElementPresentRefact(By by, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver,  timeoutInSeconds);
        return wait.until(
                ExpectedConditions.presenceOfElementLocated(by) //ожидает элемент
        );
    }

    //Возвращает true или false:Исчез элемент с экрана или присутствует
    private boolean waitForElementNotPresent(By by, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver,timeoutInSeconds);
        return wait.until(
                ExpectedConditions.invisibilityOfElementLocated(by)
        );
    }

    //Движение внутри элемента (движение влево)
    protected void swipeElementLeft(By by){
        //Ищем элемент
        WebElement element = waitForElementPresentRefact(by,5);

        //Определяем границы элемента

        //Получаем координаты верхнего левого угла элемента
        int left_x = element.getLocation().getX();
        int upper_y = element.getLocation().getY();

        //Находим верхнюю и нижнюю часть блока и делаем swipe по середине
        int right_x = left_x + element.getSize().getWidth();
        int lower_y = upper_y + element.getSize().getHeight();
        int middle_y = (upper_y + lower_y) /2;

        //Все действие описываем внутри элемента
        //Здесь описывается движение по экрану влево
        TouchAction action = new TouchAction(driver);
        action
                .press(PointOption.point(right_x,middle_y))
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)))
                .moveTo(PointOption.point(left_x,middle_y))
                .release()
                .perform();
    }

    //SCROLL по всему экрану вверх
    protected void scrollUp(int swipeTime){
        //TouchAction - позволяет описать действие
        TouchAction action = new TouchAction(driver);
        //Получаем размер всего экрана
        Dimension size = driver.manage().window().getSize();
        //Определяем размер области,в котрой будет производиться scroll
        int x = size.width/2;
        //Задаем начальную и конечную точку по "x"
        int start_y = (int)(size.height*0.9);
        int finish_y = (int)(size.height*0.2);
        action
                //Имитируем нажатие
                .press(PointOption.point(x,start_y))
                //Ждем какое-то время
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(swipeTime)))
                //определяем конечную точку движения
                .moveTo(PointOption.point(x,finish_y))
                //отправляем все это драйверу
                .release()
                //исполняем
                .perform();
    }

    //SCROLL по всему экрану вниз
    protected void scrollDown(int swipeTime){
        //TouchAction - позволяет описать действие
        TouchAction action = new TouchAction(driver);
        //Получаем размер всего экрана
        Dimension size = driver.manage().window().getSize();
        //Определяем размер области,в котрой будет производиться scroll
        int x = size.width/2;
        //Задаем начальную и конечную точку по "x"
        int start_y = (int)(size.height*0.9);
        int finish_y = (int)(size.height*0.2);
        action
                //Имитируем нажатие
                .press(PointOption.point(x,finish_y))
                //Ждем какое-то время
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(swipeTime)))
                //определяем конечную точку движения
                .moveTo(PointOption.point(x,start_y))
                //отправляем все это драйверу
                .release()
                //исполняем
                .perform();
    }

    protected void scrollUpQuik() {
        scrollUp(200);
    }

    //SCROLL до элемента
    protected void scrollForElement(By by){
        while (driver.findElements(by).size() == 0){
            scrollUpQuik();
        }
    }

    //Тест на проверку исчезновения элемента
    @Test
    public void searchTest() throws InterruptedException {
        //Ищем в главном окне кнопку "Basic + Icon + Buttons"
        WebElement basicIconButtonsButton = waitForElementPresentRefact(By.id("basic_icon"),5);
        basicIconButtonsButton.click();
        //Открывается диалоговое окно.В нем отказываемся-кнопка DISAGREE
        WebElement disagreeButton = waitForElementPresentRefact(By.id("md_button_negative"),5);
        disagreeButton.click();
        waitForElementNotPresent(By.xpath("//*[contains(@text,'Use Google's Location Services?')]"),5);
        Thread.sleep(5000);
    }

    //Тест на проверку реакции элемента на изменение ротации
    @Test
    public void rotationTest() throws InterruptedException {
        //basic_stacked_buttons
        WebElement basicStackedButtons = waitForElementPresentRefact(By.id("basic_stacked_buttons"),5);
        basicStackedButtons.click();
        Thread.sleep(2000);
        driver.rotate(ScreenOrientation.LANDSCAPE);
        Thread.sleep(4000);
    }

    //Тест на проверку реакции элементов приложения с использованием отправки приложения в бекграунд
    @Test
    public void backGroundMyTest() throws InterruptedException {
        //1)Нажимаем на кнопку "Basic"
        WebElement basic = waitForElementPresentRefact(By.id("basic"),5);
        //2)Видем контекстное окно
        WebElement window = waitForElementPresentRefact(By.id("md_scrollview_content"),5);
        //3)Сворачиваем приложение,разворачиваем
        driver.runAppInBackground(Duration.ofSeconds(3));
        //4)Окно исчезло
        WebElement windowAfterDuration = waitForElementPresentRefact(By.id("md_scrollview_content"),5);
        Thread.sleep(2000);
    }

    //Тест на проверку SWIPE
    @Test
    public void swipeTest(){
        //basic_stacked_buttons
        WebElement basicStackedButtons = waitForElementPresentRefact(By.id("basic_stacked_buttons"),5);
        basicStackedButtons.click();
        swipeElementLeft(By.id("md_button_positive"));
        //() -> swipeElementLeft(By.id("md_button_positive"))
    }

    //Тест на проверку SCROLL
    @Test
    public void scrollTest(){
        scrollForElement(By.id("basic_long_titled_buttons"));
    }
}
