# Project Title

Product API

# Project requirments

We’d like you to build the first prototype of our Product API, it must support:
Product creation and removal (standard price in EUR);
Management of product categories (Assigning and removing from products);
The ability to create products in other currencies and automatically convert to EUR (Using something like http://fixer.io).
We suggest using Java since it’s the language most of our applications are written in.
Feel free to use any technology stack you want.
Remember that good code is clean, scalable and well-tested!

## Prerequisites

The API has been made using Spring framework. The database is MySql 5.

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

Vacariuc Bogdan-Alexandru

## Description

The endpoint that are exposed are:
	- POST /products - creates a new product based on informations sent in body. The fields that can be set are: name, description, symbol, price, available, stock and image.
		The symbol specifies the currency in which the price is given, and the api will automatically transform it into EUR using a 3rd party API (exchangeratesapi.io).
		If the request could be performed, the API wil return the created product and the Http Status will be CREATED.
	- PUT /products/{productId}/categories/{categoryNames} - The productId is the id of the product that will be added in each category that is listed in categoryNames. A product can be in multiple categories in the same time.
		If the request could be performed, the API will return an ApiResponse object with the status ACCEPTED.
	- DELETE /products/{productId}/categories/{categoryNames} - The productId is the id of the product that will be deleted from each category that is listed in categoryNames. A product can be in multiple categories in the same time.
		If the request could be performed, the API will return an ApiResponse object with the status ACCEPTED.
	- DELETE /products/{id} - The product with the specified id will be deleted from the database.
		If the request could be performed, the API will return an ApiResponse object with the status ACCEPTED and the HTTPStatus OK.
	- PATCH /products/{id} - The product with the specified id will be updated with the values specified in the request body. The keys that are not specified will remain the same.
	- GET /products/{id} - The response body will contain the product with the specified id.
	- GET /products - The response body will contain a list with all products.
	- GET /categories - The response body will contain a list with all categories(id, name and description).
	- POST /categories - The category with the specified name and description will be created. The response body will contain the newly created category.
	- GET /products/categories/{categoryNames} - The response body will contain a list of all the products that are available in the specified categories.
	
If one ore more errors appear, the api wil return an ApiResponse object, containing a timestamp, a status, a message and a list of errors that occured. Each error has details, the rejected object and the rejected field of the request.


