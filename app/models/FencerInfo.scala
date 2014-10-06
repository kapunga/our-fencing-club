package models

import org.kapunga.fredconnect.Fencer

case class FencerInfo(uid: Long) {
  def this(fredFencer: Fencer) = {
    this(-1)
  }
}

object FencerInfo {

}


