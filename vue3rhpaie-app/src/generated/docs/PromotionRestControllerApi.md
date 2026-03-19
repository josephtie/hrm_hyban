# PromotionRestControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deletePromotion**](#deletepromotion) | **POST** /api/rh/carriere/promotions/supprimer | |
|[**findPromotion**](#findpromotion) | **POST** /api/rh/carriere/promotions/trouver | |
|[**findPromotions**](#findpromotions) | **POST** /api/rh/carriere/promotions/lister | |
|[**getPromotionList**](#getpromotionlist) | **GET** /api/rh/carriere/promotions/list | |
|[**getSocietes2**](#getsocietes2) | **GET** /api/rh/carriere/promotions/societes | |
|[**savePromotion**](#savepromotion) | **POST** /api/rh/carriere/promotions/enregistrer | |
|[**viewPromotions**](#viewpromotions) | **GET** /api/rh/carriere/promotions/view | |

# **deletePromotion**
> PromotionDTO deletePromotion(promotionRequest)


### Example

```typescript
import {
    PromotionRestControllerApi,
    Configuration,
    PromotionRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PromotionRestControllerApi(configuration);

let promotionRequest: PromotionRequest; //

const { status, data } = await apiInstance.deletePromotion(
    promotionRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **promotionRequest** | **PromotionRequest**|  | |


### Return type

**PromotionDTO**

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

# **findPromotion**
> PromotionDTO findPromotion(promotionRequest)


### Example

```typescript
import {
    PromotionRestControllerApi,
    Configuration,
    PromotionRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PromotionRestControllerApi(configuration);

let promotionRequest: PromotionRequest; //

const { status, data } = await apiInstance.findPromotion(
    promotionRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **promotionRequest** | **PromotionRequest**|  | |


### Return type

**PromotionDTO**

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

# **findPromotions**
> PromotionDTO findPromotions()


### Example

```typescript
import {
    PromotionRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PromotionRestControllerApi(configuration);

const { status, data } = await apiInstance.findPromotions();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**PromotionDTO**

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

# **getPromotionList**
> PromotionResponseObject getPromotionList()


### Example

```typescript
import {
    PromotionRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PromotionRestControllerApi(configuration);

let limit: number; // (optional) (default to 10)
let offset: number; // (optional) (default to 0)
let search: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getPromotionList(
    limit,
    offset,
    search
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **limit** | [**number**] |  | (optional) defaults to 10|
| **offset** | [**number**] |  | (optional) defaults to 0|
| **search** | [**string**] |  | (optional) defaults to undefined|


### Return type

**PromotionResponseObject**

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

# **getSocietes2**
> Array<Societe> getSocietes2()


### Example

```typescript
import {
    PromotionRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PromotionRestControllerApi(configuration);

const { status, data } = await apiInstance.getSocietes2();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<Societe>**

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

# **savePromotion**
> PromotionDTO savePromotion(promotionRequest)


### Example

```typescript
import {
    PromotionRestControllerApi,
    Configuration,
    PromotionRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PromotionRestControllerApi(configuration);

let promotionRequest: PromotionRequest; //

const { status, data } = await apiInstance.savePromotion(
    promotionRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **promotionRequest** | **PromotionRequest**|  | |


### Return type

**PromotionDTO**

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

# **viewPromotions**
> string viewPromotions()


### Example

```typescript
import {
    PromotionRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PromotionRestControllerApi(configuration);

const { status, data } = await apiInstance.viewPromotions();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**string**

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

