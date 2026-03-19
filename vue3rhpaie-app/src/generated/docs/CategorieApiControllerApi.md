# CategorieApiControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deleteCategory2**](#deletecategory2) | **POST** /api/categories/supprimercategorie | |
|[**getCategory**](#getcategory) | **POST** /api/categories/affichcategorie | |
|[**getCategoryList**](#getcategorylist) | **GET** /api/categories/listcategorie | |
|[**getCategoryListJSON1**](#getcategorylistjson1) | **POST** /api/categories/listcategoriejson | |
|[**saveCategory1**](#savecategory1) | **POST** /api/categories | |

# **deleteCategory2**
> CategorieDTO deleteCategory2(idRequest)


### Example

```typescript
import {
    CategorieApiControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new CategorieApiControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.deleteCategory2(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**CategorieDTO**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getCategory**
> Categorie getCategory(idRequest)


### Example

```typescript
import {
    CategorieApiControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new CategorieApiControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.getCategory(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**Categorie**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getCategoryList**
> Array<Categorie> getCategoryList()


### Example

```typescript
import {
    CategorieApiControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new CategorieApiControllerApi(configuration);

const { status, data } = await apiInstance.getCategoryList();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<Categorie>**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getCategoryListJSON1**
> CategorieDTO getCategoryListJSON1(paginationRequest)


### Example

```typescript
import {
    CategorieApiControllerApi,
    Configuration,
    PaginationRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new CategorieApiControllerApi(configuration);

let paginationRequest: PaginationRequest; //

const { status, data } = await apiInstance.getCategoryListJSON1(
    paginationRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **paginationRequest** | **PaginationRequest**|  | |


### Return type

**CategorieDTO**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **saveCategory1**
> CategorieDTO saveCategory1(requestCategory)


### Example

```typescript
import {
    CategorieApiControllerApi,
    Configuration,
    RequestCategory
} from './api';

const configuration = new Configuration();
const apiInstance = new CategorieApiControllerApi(configuration);

let requestCategory: RequestCategory; //

const { status, data } = await apiInstance.saveCategory1(
    requestCategory
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **requestCategory** | **RequestCategory**|  | |


### Return type

**CategorieDTO**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

