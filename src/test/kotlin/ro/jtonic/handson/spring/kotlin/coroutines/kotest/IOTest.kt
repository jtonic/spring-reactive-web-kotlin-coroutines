package ro.jtonic.handson.spring.kotlin.coroutines.kotest

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import java.io.File
import kotlin.io.path.createTempFile

@JvmInline
value class ClasspathDirName(val name: String)

fun ClasspathDirName.concatAllFilesContentToTmpFile(prefixTmpName: String, suffixTmpName: String): File = let {
    val tmpDest = createTempFile(prefixTmpName, suffixTmpName).toFile()
    println("tmpDest = $tmpDest")

    val srcDir = Thread.currentThread().contextClassLoader.getResource(this.name)?.run {
        File(this.toURI())
    } ?: throw RuntimeException("Resource not found for path: ${this.name}")
    println("scrDir = $srcDir")

    tmpDest.writer().use { writer ->
        srcDir.listFiles()?.sorted()?.forEach {
            writer.write(it.readText())
        }
    }
    tmpDest
}

class IOTest : FreeSpec({

    "concat many files content in a single one in tmp folder" {

        val tmpFile = ClasspathDirName("cql").concatAllFilesContentToTmpFile("SRP_", ".cql")

        println(
            """
           TmpDest content:
           ${tmpFile.readText()}
        """.trimIndent()
        )
        tmpFile.readText() shouldBe """
            -- aa
            -- bb
            -- jj
            -- pp
            
            """.trimIndent()
    }
})