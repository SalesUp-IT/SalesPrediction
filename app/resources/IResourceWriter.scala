package resources

import java.io.{File, FileWriter}

trait IResourceWriter extends IResourceReader {

  def write(directoryName: String, fileName: String, body: String): Unit = {
    val directoryPath = directoryName.split("/").toList
    val directory = getResourceAsFile(directoryPath.head)
    new File(directory, directoryPath.tail.mkString("/")).mkdirs()
    val target = getResourceAsFile(directoryName)
    val file = new File(target, fileName)
    val writer = new FileWriter(file)
    writer.write(body)
    writer.flush()
    writer.close()
  }
}
