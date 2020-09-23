package edu.ncsu.csc.itrust.model.old.enums;

/**
 * Vaginal Delivery,
 * Vaginal Delivery Vacuum Assist,
 * Vaginal Delivery Forceps Assist,
 * Caesarean Section,
 * Miscarriage,
 * Not specified.
 */
public enum DeliveryType
{
	VaginalDelivery("Vaginal Delivery"),
	VaginalDeliveryVacuumAssist("Vaginal Delivery Vacuum Assist"),
	VaginalDeliveryForcepsAssist("Vaginal Delivery Forceps Assist"),
	CaesareanSection("Caesarean Section"),
	Miscarriage("Miscarriage"),
	NotSpecified("Not Specified");
	private String name;

	private DeliveryType(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	@Override
	public String toString()
	{
		return getName();
	}

	public static DeliveryType parse(String input)
	{
		for (DeliveryType deliveryType : DeliveryType.values())
		{
			if (deliveryType.name.equals(input))
			{
				return deliveryType;
			}
		}

		return NotSpecified;
	}
}