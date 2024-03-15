/*
 * This file is part of ImageServer.
 *
 * ImageServer is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later 
 * version.
 *
 * ImageServer is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for 
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * ImageServer. If not, see <https://www.gnu.org/licenses/>. 
 */

package containers;

/**
 * Container of information related to an image file, the name and the number of files available.
 */
public class GetRandomFileResult {
	private static final String whoAmI = GetRandomFileResult.class.getName();	
	
	private Integer availableImages;
	
	private String randomPathFileName;
	
	/**
	 * Constructor, given:
	 * <p>
	 * @param availableImages The number of available images.
	 * @param randomPathFileName String containing the path/nName of an image file.
	 */
	public GetRandomFileResult(Integer availableImages, String randomPathFileName) {
		this.availableImages    = availableImages;
		this.randomPathFileName = randomPathFileName;	
	}
	
	/**
	 * Get the number of available files.
	 * <p>
	 * @return The number of available images.
	 */                          	
	public Integer getAvailableImages() {
		return availableImages;
	}
	
	/**
	 * Get the path/name of an random image.
	 * <p>
	 * @return String containing the path/name of a random image file.
	 */  	
	public String getRandomPathFileName() {
		return randomPathFileName;
	}
}
	