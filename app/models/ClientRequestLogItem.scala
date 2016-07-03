package models

case class ClientRequestLogItem(ip: String, url: String, status: Int, method: String, currentTimeMillis: Long = System.currentTimeMillis)
