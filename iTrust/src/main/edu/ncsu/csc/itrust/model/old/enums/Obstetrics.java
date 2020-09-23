package edu.ncsu.csc.itrust.model.old.enums;

/**
 * Eligible, Ineligible, or Not specified.
 */
public enum Obstetrics
{
	Eligible("Eligible"), Ineligible("Ineligible"), NotSpecified("Not Specified");
	private String name;

	private Obstetrics(String name)
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

	public static Obstetrics parse(String input)
	{
		for (Obstetrics obstetrics : Obstetrics.values())
		{
			if (obstetrics.name.equals(input))
			{
				return obstetrics;
			}
		}

		return NotSpecified;
	}
}