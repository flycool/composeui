package com.compose.sample.composeui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.runtime.snapshots.SnapshotStateObserver
import com.compose.sample.kotlin.html.getGistCode2
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import org.junit.Assert.*
import org.junit.Test
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import kotlin.system.measureTimeMillis

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {


    @Test
    fun htmlUnitTest() {
//        val url = "https://www.qidian.com/"
//        val webClient = WebClient().apply {
//            //options.isThrowExceptionOnScriptError = false
//        }
//        val page = webClient.getPage<HtmlPage>(url)
//        val title = page.getElementByName<>()
//        println("title==${title}")
//        println("div==${div}")
//        println("iframe==${iframe}")
//        page.frames.forEach { frame->
//            val src = frame.frameElement.srcAttribute
//            println("frame src===$src")
//        }
//        println("done======")



//        val url = "https://www.htmlunit.org/"
//        val url = "https://www.qidian.com/"
//        val url = "https://gist.githubusercontent.com/LloydBlv/22d2a02330a2c831fab81f84c357bc66/raw/32e50b4b3bd1d133017d8069c407e8f979e71641/MutableStateExpose.kt"
//        val url = "https://proandroiddev.com/media/738b139c6cf991fc8693b02d8c32022a"
        val url =
            "https://proandroiddev.com/mastering-android-viewmodels-essential-dos-and-donts-part-2-%EF%B8%8F-2b49281f0029"


        val options = ChromeOptions().apply {
            setBinary("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe")
//             addArguments ("--headless", "--disable-gpu");

        }
        System.setProperty(
            "webdriver.chrome.driver",
            "F:\\uiwork\\composeui\\app\\src\\test\\java\\com\\compose\\sample\\composeui\\chromedriver.exe"
        )

        val driver = ChromeDriver(options)
        driver.get(url)

        (driver as JavascriptExecutor).executeScript("window.scrollTo(0, document.body.scrollHeight/3)")
        Thread.sleep(2000);
        (driver as JavascriptExecutor).executeScript("window.scrollTo(0, (document.body.scrollHeight)*2/3)")
        Thread.sleep(2000);
        (driver as JavascriptExecutor).executeScript("window.scrollTo(0, document.body.scrollHeight)")
        Thread.sleep(2000);
        (driver as JavascriptExecutor).executeScript("window.scrollTo(document.body.scrollHeight, 0)")

        val html = driver.pageSource

        driver.quit()


        val doc = Jsoup.parse(html)
        val iframes = doc.getElementsByTag("iframe")

        println("size====${iframes.size}")


        iframes.forEach { frame ->

//            val ge: Elements? = frame.getElementsByClass("gist-meta")
//            println("ge====$ge")
//            ge?.let {
//                val gistMeta = it[0]
//                val ae = gistMeta.getElementsByTag("a")[0]
//                val link = ae.attr("href")
//                runBlocking {
//                    val gistCode2 = getGistCode2(link)
//                    println("gistcode====$gistCode2")
//                }
//            }

        }
//
//
//        val codemeta = doc.getElementsByClass("gist-meta")
//        println("codemeta==$codemeta")

//        WebClient(BrowserVersion.CHROME).use { webClient ->
//
//            webClient.options.isThrowExceptionOnScriptError = true
//            webClient.options.isActiveXNative = false
//            webClient.options.isCssEnabled = true
//            webClient.options.isJavaScriptEnabled = true
//            webClient.ajaxController = NicelyResynchronizingAjaxController()
//
//
//
////            val url = "https://www.qidian.com/"
////            val url = "https://www.htmlunit.org/"
////            val url = "https://proandroiddev.com/"
//            val page = webClient.getPage<HtmlPage>(url)
//            val title = page.getElementsByName("title")
//
//            val asXml = page.asXml()
//            println("asxml==$asXml")
//        }

    }


    fun dataFlow() = flowOf(1, 2)

    //don't do this
    fun <T> Flow<T>.handleError(): Flow<T> = flow {
        try {
            collect { value -> emit(value) }
        } catch (e: Exception) {
            // 这里异常被catch，下游或collector就无法获取到异常
            println("catch error: ${e.message}")
        }
    }

    fun <T> Flow<T>.rightHandleError(): Flow<T> =
        catch { e -> println("right catch error: ${e.message}") }

    @Test
    fun flowExceptionTest() {
        runBlocking {
            dataFlow()
                .onEach {
                    println("oneach value: $it")
                    error("onEach error")
                }
                .rightHandleError()
                .collect()
        }

        dataFlow()
            .onEach { println("oneach value: $it") }
            .rightHandleError()
            .launchIn(MainScope())
    }


    @Test
    fun coroutineContextTest() {
        runBlocking {
            println("my context is $coroutineContext")
            println("a context with name: ${coroutineContext + CoroutineName("test")}")
            println("a context with name: ${coroutineContext[CoroutineName]}")
            println("my job is : ${coroutineContext[Job]}")

            println("outer scope: $this@runBlocking")

            val job = launch(CoroutineName("child")) {
                println("inner scope: $this@launch")
                println("launch context: ${coroutineContext}")
                println("launch context job: ${coroutineContext[Job]}")
                println("launch context name: ${coroutineContext[CoroutineName]}")

            }
            println("launch job: $job")

        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun <T> Flow<T>.buffer(size: Int = 0): Flow<T> = flow {
        coroutineScope {
            println("11111")
            val channel = produce(capacity = size) {
                collect {
                    println("33333")
                    send(it)
                }
            }
            channel.consumeEach {
                println("44444")
                emit(it)
            }
        }
    }

    @Test
    fun flowTest() {
        runBlocking {
            println("main thread: ==" + Thread.currentThread().name)
            val flow = flow {
                println("flow block thread:==" + Thread.currentThread().name)
                println("22222")
                for (i in 1..2) {
                    delay(100)
                    emit(i)
                }
            }.flowOn(Dispatchers.Default)
            val time = measureTimeMillis {
                //此时，emitter 与 collector 分开了，它们并行运行。emitter 是在另一个协程中运行的
                flow.buffer().collect {
                    println("collect thread:==" + Thread.currentThread().name)
                    delay(100)
                    println(it)
                }
            }
            println("collected in $time ms")
        }
    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun mutableTest() {
        val list1 = mutableListOf("a")
        val list2 = list1
        list2.add("b")
        assert(list1 === list2)
        assert(list1 == list2)
        assert(list1 == mutableListOf("a", "b"))

        val alist = mutableStateListOf("a")
        alist.add("b")
        val blist = alist
        assertEquals(alist, blist)
    }


    @Test
    fun snapshot() {
        val dog = Dog()
        dog.name.value = "Spot"

        fun immediateExecutor(runnable: () -> Unit) {
            runnable()
        }


        fun blockObserver() {
            println("dog name: ${dog.name.value}")
        }


        val observer = SnapshotStateObserver(::immediateExecutor)

        fun onChange(scope: Int) {
            println("something was changed from pass $scope")
            println("performing next read pass")
            observer.observeReads(
                scope = scope + 1,
                onValueChangedForScope = ::onChange,
                block = ::blockObserver
            )
        }

        observer.observeReads(
            scope = 0,
            onValueChangedForScope = ::onChange,
            block = ::blockObserver
        )

        observer.start()

        Snapshot.withMutableSnapshot {
            dog.name.value = "Fido"
        }

        observer.stop()

    }


    class Dog {
        val name: MutableState<String> = mutableStateOf("")
    }


}