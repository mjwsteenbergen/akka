/**
 * Copyright (C) 2009-2018 Lightbend Inc. <https://www.lightbend.com>
 */
package akka.actor.typed.receptionist

import akka.actor.typed.TypedAkkaSpecWithShutdown
import akka.actor.typed.internal.ActorRefSerializationSpec
import akka.actor.typed.internal.receptionist.ServiceKeySerializer
import akka.actor.typed.scaladsl.adapter._
import akka.serialization.SerializationExtension
import akka.testkit.typed.scaladsl.ActorTestKit

class ServiceKeySerializationSpec extends ActorTestKit with TypedAkkaSpecWithShutdown {

  override def config = ActorRefSerializationSpec.config

  val serialization = SerializationExtension(system.toUntyped)

  "ServiceKey[T]" must {
    "be serialized and deserialized by ServiceKeySerializer" in {
      val obj = ServiceKey[Int]("testKey")
      serialization.findSerializerFor(obj) match {
        case serializer: ServiceKeySerializer ⇒
          val blob = serializer.toBinary(obj)
          val ref = serializer.fromBinary(blob, serializer.manifest(obj))
          ref should be(obj)
        case s ⇒
          throw new IllegalStateException(s"Wrong serializer ${s.getClass} for ${obj.getClass}")
      }
    }
  }
}
