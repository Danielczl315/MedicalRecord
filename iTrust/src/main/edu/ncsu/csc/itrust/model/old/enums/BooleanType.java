package edu.ncsu.csc.itrust.model.old.enums;

/**
 * True, False, or Not specified.
 */
public enum BooleanType
{
	True("True"), False("False"), NotSpecified("Not Specified");
	private String name;

	private BooleanType(String name)
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

	public static BooleanType parse(String input)
	{
		for (BooleanType bool : BooleanType.values())
		{
			if (bool.name.equals(input))
			{
				return bool;
			}
		}

		return NotSpecified;
	}
}