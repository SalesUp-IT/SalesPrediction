package resources

import java.io.{File, FileInputStream, InputStream}

trait IResourceReader {

  def getResourceAsStream(name: String): InputStream = {
    getClass.getResourceAsStream(name)
  }

  def getResourceAsFile(name: String): File = {
    val file = getClass.getClassLoader.getResource(name).getFile
    new File(file)
  }

  def getResourceInExternalForm(name:String): String = {
    getClass.getClassLoader.getResource(name).toExternalForm
  }

  def getAllFilesFromDirectory(directory: String): Seq[File] = {
    val file = getResourceAsFile(directory)
    if (file.exists()) {
      if (file.isDirectory) {
        file.listFiles().toSeq
      } else {
        Seq.empty
      }
    } else Seq.empty
  }

  def getLinesFromFile(file: File): Seq[String] = {
    val stream = new FileInputStream(file)
    scala.io.Source.fromInputStream(stream).getLines.toSeq
  }

}
