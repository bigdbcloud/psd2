package com.ibm.psd2.commons.datamodel;

/**
 * <p>
 * Represents a point on the surface of a sphere. (The Earth is almost
 * spherical.)
 * </p>
 * 
 * <p>
 * To create an instance, call one of the static methods fromDegrees() or
 * fromRadians().
 * </p>
 * 
 * <p>
 * This code was originally published at <a
 * href="http://JanMatuschek.de/LatitudeLongitudeBoundingCoordinates#Java">
 * http://JanMatuschek.de/LatitudeLongitudeBoundingCoordinates#Java</a>.
 * </p>
 * 
 */

public class GeoLocation
{

	private double latitude; // latitude in degrees
	private double longitude; // longitude in degrees

	private double latitudeInRadians;
	private double longitudeInRadians;

	public static final double MIN_LAT = Math.toRadians(-90d); // -PI/2
	public static final double MAX_LAT = Math.toRadians(90d); // PI/2
	public static final double MIN_LON = Math.toRadians(-180d); // -PI
	public static final double MAX_LON = Math.toRadians(180d); // PI

	public GeoLocation()
	{
	}

	public GeoLocation(double latitude, double longitude)
	{
		this.latitude = latitude;
		this.longitude = longitude;
		this.latitudeInRadians = Math.toRadians(latitude);
		this.longitudeInRadians = Math.toRadians(longitude);
		checkBounds();
	}

	private void checkBounds()
	{

		if (latitudeInRadians < MIN_LAT || latitudeInRadians > MAX_LAT
				|| longitudeInRadians < MIN_LON || longitudeInRadians > MAX_LON)
			throw new IllegalArgumentException("Invalid Coordinates");
	}

	public double getLatitude()
	{
		return latitude;
	}

	public void setLatitude(double latitude)
	{
		this.latitude = latitude;
		this.latitudeInRadians = Math.toRadians(latitude);
		checkBounds();
	}

	public double getLongitude()
	{
		return longitude;
	}

	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
		this.longitudeInRadians = Math.toRadians(longitude);
		checkBounds();
	}

	public double getLatitudeInRadians()
	{
		return latitudeInRadians;
	}

	public double getLongitudeInRadians()
	{
		return longitudeInRadians;
	}

}