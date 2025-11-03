package ro.jtonic.handson.spring.kotlin.coroutines.kotest

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import java.io.File
import kotlin.io.path.createTempFile

@JvmInline
value class ClasspathDirName(val name: String = "." /* default to root */)

fun ClasspathDirName.concatAllFilesContentToTmpFile(
    prefixTmpName: String,
    suffixTmpName: String,
    extension: String
): File = let {
    val tmpDest = createTempFile(prefixTmpName, suffixTmpName).toFile()
    println("tmpDest = $tmpDest")

    val srcDir = Thread.currentThread().contextClassLoader.getResource(this.name)?.run {
        File(this.toURI())
    } ?: throw RuntimeException("Resource not found for path: ${this.name}")
    println("scrDir = $srcDir")

    tmpDest.writer().use { writer ->
        srcDir.listFiles()?.filter { it.isFile && it.extension == extension }?.sorted()?.forEach {
            writer.write(it.readText())
        }
    }
    tmpDest
}

class IOTest : FreeSpec({

    "test file contents concatenation in a temp file" - {

        mapOf(
            "." to "-- qq\n",
            "cql" to """
            -- aa
            -- bb
            -- jj
            -- pp
            
            """.trimIndent()
        ).forEach { (path, expectedContent) ->

            "concat many files content in a single one in tmp folder" {

                val tmpFile = ClasspathDirName(path).concatAllFilesContentToTmpFile("SRP_", ".cql", extension = "cql")

                println(
                    """
        TmpDest content:
        ${tmpFile.readText()}
        """.trimIndent()
                )
                tmpFile.readText() shouldBe expectedContent
            }
        }
    }
})