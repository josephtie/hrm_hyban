# PromotionPersonnelRestControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deletePromotionPersonnel**](#deletepromotionpersonnel) | **POST** /api/rh/carriere/promotions-personnel/supprimer | |
|[**findPromotionPersonnel**](#findpromotionpersonnel) | **POST** /api/rh/carriere/promotions-personnel/trouver | |
|[**findPromotionPersonnels**](#findpromotionpersonnels) | **POST** /api/rh/carriere/promotions-personnel/lister | |
|[**findPromotionPersonnelsByPersonnel**](#findpromotionpersonnelsbypersonnel) | **POST** /api/rh/carriere/promotions-personnel/lister-par-personnel | |
|[**findPromotionPersonnelsByPromotion**](#findpromotionpersonnelsbypromotion) | **POST** /api/rh/carriere/promotions-personnel/lister-par-promotion | |
|[**getPromotionPersonnelList**](#getpromotionpersonnellist) | **GET** /api/rh/carriere/promotions-personnel/list | |
|[**savePromotionPersonnel**](#savepromotionpersonnel) | **POST** /api/rh/carriere/promotions-personnel/enregistrer | |
|[**viewPromotionsPersonnel**](#viewpromotionspersonnel) | **GET** /api/rh/carriere/promotions-personnel/view | |

# **deletePromotionPersonnel**
> PromotionPersonnelDTO deletePromotionPersonnel(promotionPersonnelRequest)


### Example

```typescript
import {
    PromotionPersonnelRestControllerApi,
    Configuration,
    PromotionPersonnelRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PromotionPersonnelRestControllerApi(configuration);

let promotionPersonnelRequest: PromotionPersonnelRequest; //

const { status, data } = await apiInstance.deletePromotionPersonnel(
    promotionPersonnelRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **promotionPersonnelRequest** | **PromotionPersonnelRequest**|  | |


### Return type

**PromotionPersonnelDTO**

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

# **findPromotionPersonnel**
> PromotionPersonnelDTO findPromotionPersonnel(promotionPersonnelRequest)


### Example

```typescript
import {
    PromotionPersonnelRestControllerApi,
    Configuration,
    PromotionPersonnelRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PromotionPersonnelRestControllerApi(configuration);

let promotionPersonnelRequest: PromotionPersonnelRequest; //

const { status, data } = await apiInstance.findPromotionPersonnel(
    promotionPersonnelRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **promotionPersonnelRequest** | **PromotionPersonnelRequest**|  | |


### Return type

**PromotionPersonnelDTO**

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

# **findPromotionPersonnels**
> PromotionPersonnelDTO findPromotionPersonnels()


### Example

```typescript
import {
    PromotionPersonnelRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PromotionPersonnelRestControllerApi(configuration);

const { status, data } = await apiInstance.findPromotionPersonnels();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**PromotionPersonnelDTO**

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

# **findPromotionPersonnelsByPersonnel**
> PromotionPersonnelDTO findPromotionPersonnelsByPersonnel(promotionPersonnelRequest)


### Example

```typescript
import {
    PromotionPersonnelRestControllerApi,
    Configuration,
    PromotionPersonnelRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PromotionPersonnelRestControllerApi(configuration);

let promotionPersonnelRequest: PromotionPersonnelRequest; //

const { status, data } = await apiInstance.findPromotionPersonnelsByPersonnel(
    promotionPersonnelRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **promotionPersonnelRequest** | **PromotionPersonnelRequest**|  | |


### Return type

**PromotionPersonnelDTO**

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

# **findPromotionPersonnelsByPromotion**
> PromotionPersonnelDTO findPromotionPersonnelsByPromotion(promotionPersonnelRequest)


### Example

```typescript
import {
    PromotionPersonnelRestControllerApi,
    Configuration,
    PromotionPersonnelRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PromotionPersonnelRestControllerApi(configuration);

let promotionPersonnelRequest: PromotionPersonnelRequest; //

const { status, data } = await apiInstance.findPromotionPersonnelsByPromotion(
    promotionPersonnelRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **promotionPersonnelRequest** | **PromotionPersonnelRequest**|  | |


### Return type

**PromotionPersonnelDTO**

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

# **getPromotionPersonnelList**
> PromotionPersonnelResponseObject getPromotionPersonnelList()


### Example

```typescript
import {
    PromotionPersonnelRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PromotionPersonnelRestControllerApi(configuration);

let limit: number; // (optional) (default to 10)
let offset: number; // (optional) (default to 0)
let search: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getPromotionPersonnelList(
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

**PromotionPersonnelResponseObject**

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

# **savePromotionPersonnel**
> PromotionPersonnelDTO savePromotionPersonnel(promotionPersonnelRequest)


### Example

```typescript
import {
    PromotionPersonnelRestControllerApi,
    Configuration,
    PromotionPersonnelRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PromotionPersonnelRestControllerApi(configuration);

let promotionPersonnelRequest: PromotionPersonnelRequest; //

const { status, data } = await apiInstance.savePromotionPersonnel(
    promotionPersonnelRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **promotionPersonnelRequest** | **PromotionPersonnelRequest**|  | |


### Return type

**PromotionPersonnelDTO**

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

# **viewPromotionsPersonnel**
> string viewPromotionsPersonnel()


### Example

```typescript
import {
    PromotionPersonnelRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PromotionPersonnelRestControllerApi(configuration);

const { status, data } = await apiInstance.viewPromotionsPersonnel();
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

