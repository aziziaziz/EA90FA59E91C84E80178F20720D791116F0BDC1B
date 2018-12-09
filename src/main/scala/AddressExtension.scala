import akka.actor.{ActorSystem, Address, ExtendedActorSystem, Extension, ExtensionId}

class AddressExtension(system: ExtendedActorSystem) extends Extension {
  val address: Address = system.provider.getDefaultAddress
}

object AddressExtension extends ExtensionId[AddressExtension] {
  def createExtension(system: ExtendedActorSystem): AddressExtension = new AddressExtension(system)

  def hostOf(system: ActorSystem): String = AddressExtension(system).address.host.getOrElse("")
  def portOf(system: ActorSystem): Int    = AddressExtension(system).address.port.getOrElse(0)
}