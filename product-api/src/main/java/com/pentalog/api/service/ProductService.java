package com.pentalog.api.service;

import java.util.List;
import java.util.Map;

import com.pentalog.api.dto.ProductDTO;
import com.pentalog.api.model.Product;

public interface ProductService {

	/**
	 * Adds a product to the database with the given informations.
	 * 
	 * @param productDTO
	 * @return newly created Product object
	 * @throws ObjectNotFoundException if the name is taken, null or empty string or
	 *                                 if the price could not be converted
	 */
	Product addProduct(ProductDTO productDTO);

	/**
	 * Adds a product in the specified categories
	 * 
	 * @param productId     Id of the product which will be added in the specified
	 *                      categories
	 * @param categoryNames The names of the categories
	 * @throws ObjectNotFoundException if any of the categories provided have not
	 *                                 been found
	 */
	void addCategoryToProduct(Integer productId, List<String> categoryNames);

	/**
	 * Removes a product from the specified categories
	 * 
	 * @param productId     Id of the product which will be removed from the
	 *                      specified categories
	 * @param categoryNames The names of the categories
	 * @throws ObjectNotFoundException if any of the categories provided have not
	 *                                 been found
	 */
	void removeCategoriesFromProduct(Integer productId, List<String> categoryNames);

	/**
	 * Deletes the product with the specified id from the database.
	 * 
	 * @param productId id of the product that has to be deleted
	 * @throws ObjectNotFoundException if the id is not valid
	 */
	void removeProduct(Integer productId);

	/**
	 * Return the product with the specified id and checks for its existance.
	 * 
	 * @param productId
	 * @return the Product object found in the database
	 * @throws ObjectNotFoundException if the product was not found
	 */
	Product getProductByIdAndCheckExistance(Integer productId);

	/**
	 * Returns all the products stored in the database.
	 * 
	 * @return a list of products
	 */
	List<Product> getAllProducts();

	/**
	 * Updated the product given by id.
	 * 
	 * @param productId  the id of the product that has to be updated
	 * @param productDTO a map of fields - values that have to be updated in the
	 *                   product object
	 * @return updated product
	 */
	Product updateProductById(Integer productId, Map<String, Object> productDTO);

	/**
	 * Returns a list containing the products that are in the specified categories.
	 * 
	 * @param categoryNames the categories that the products must be in
	 * @return a list of products
	 */
	List<Product> getProductsForCategories(List<String> categoryNames);
}
