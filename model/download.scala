def clean(): Unit = {
  new java.io.File(".").list
      .filter(_ endsWith ".exe") foreach { old =>
    val oldVersion = testVersion(old)
    if (new java.io.File(old).delete()) {
      println(s"Deleted $old ($oldVersion) ...")
    } else {
      println(s"Could not delete: $old")
    }
  }
}

def download() = {
  val url = "https://compiler.dsl-platform.com:8443/platform/download/dsl-compiler.zip"

  val zis = new java.util.zip.ZipInputStream(
    new java.io.BufferedInputStream(
    new java.net.URL(url).openStream()))

  val firstEntry = zis.getNextEntry
  assert(firstEntry.getName == "dsl-compiler.exe")

  val buffer = new Array[Byte](firstEntry.getSize.toInt)
  val tempFile = java.util.UUID.randomUUID + ".exe"
  val fos = new java.io.FileOutputStream(tempFile)

  def slurp(soFar: Int = 0): Int = {
    val read = zis.read(buffer)
    if (read != -1) {
      val total = soFar + read
      fos.write(buffer, 0, read)
      println(s"Wrote $total bytes ...")
      slurp(total)
    } else {
      soFar
    }
  }
  val size = slurp()
  assert(size == buffer.length, "Size mismatch!")

  zis.close()
  fos.close()
  (tempFile, size)
}

def testVersion(tempFile: String): String = {
  import sys.process._

  val stdout = new java.io.ByteArrayOutputStream
  val stderr = new java.io.ByteArrayOutputStream
  val stdoutWriter = new java.io.PrintWriter(stdout)
  val stderrWriter = new java.io.PrintWriter(stderr)
  val exitValue = tempFile ! ProcessLogger(stdoutWriter.println, stderrWriter.println)
  stdoutWriter.close()
  stderrWriter.close()

  val VersionPattern = """(?s).*?Version: (\d+(?:\.\d+)+).*?""".r
  val VersionPattern(version) = stdout.toString
  version
}

def rename(tempFile: String, version: String) = {
  val sourceFile = new java.io.File(tempFile)
  val targetFile = new java.io.File(s"dsl-compiler-$version.exe")
  sourceFile renameTo targetFile
  println(s"Renamed to $targetFile")
}

// Clean all the things!
clean()

// Download compiler
val (tempFile, size) = download()
println(s"Wrote $tempFile ($size bytes)")

// Check version
val version = testVersion(tempFile)
println(s"Compiler version: $version")

// Rename temporary file name
rename(tempFile, version)

// Exit to OS
sys.exit(0)
