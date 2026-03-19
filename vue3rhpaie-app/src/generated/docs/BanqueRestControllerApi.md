# BanqueRestControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deleteBanque**](#deletebanque) | **POST** /api/parametrages/banques/supprimer | |
|[**findBanque**](#findbanque) | **POST** /api/parametrages/banques/trouver | |
|[**findBanques**](#findbanques) | **POST** /api/parametrages/banques/lister | |
|[**getBanqueList**](#getbanquelist) | **GET** /api/parametrages/banques/list | |
|[**getSocietes9**](#getsocietes9) | **GET** /api/parametrages/banques/societes | |
|[**saveBanque**](#savebanque) | **POST** /api/parametrages/banques/enregistrer | |
|[**viewBanques**](#viewbanques) | **GET** /api/parametrages/banques/view | |

# **deleteBanque**
> BanqueDTO deleteBanque(banqueRequest)


### Example

```typescript
import {
    BanqueRestControllerApi,
    Configuration,
    BanqueRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new BanqueRestControllerApi(configuration);

let banqueRequest: BanqueRequest; //

const { status, data } = await apiInstance.deleteBanque(
    banqueRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **banqueRequest** | **BanqueRequest**|  | |


### Return type

**BanqueDTO**

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

# **findBanque**
> BanqueDTO findBanque(banqueRequest)


### Example

```typescript
import {
    BanqueRestControllerApi,
    Configuration,
    BanqueRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new BanqueRestControllerApi(configuration);

let banqueRequest: BanqueRequest; //

const { status, data } = await apiInstance.findBanque(
    banqueRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **banqueRequest** | **BanqueRequest**|  | |


### Return type

**BanqueDTO**

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

# **findBanques**
> BanqueDTO findBanques()


### Example

```typescript
import {
    BanqueRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new BanqueRestControllerApi(configuration);

const { status, data } = await apiInstance.findBanques();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**BanqueDTO**

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

# **getBanqueList**
> BanqueResponseObject getBanqueList()


### Example

```typescript
import {
    BanqueRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new BanqueRestControllerApi(configuration);

let limit: number; // (optional) (default to 10)
let offset: number; // (optional) (default to 0)
let search: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getBanqueList(
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

**BanqueResponseObject**

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

# **getSocietes9**
> Array<Societe> getSocietes9()


### Example

```typescript
import {
    BanqueRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new BanqueRestControllerApi(configuration);

const { status, data } = await apiInstance.getSocietes9();
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

# **saveBanque**
> BanqueDTO saveBanque(banqueRequest)


### Example

```typescript
import {
    BanqueRestControllerApi,
    Configuration,
    BanqueRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new BanqueRestControllerApi(configuration);

let banqueRequest: BanqueRequest; //

const { status, data } = await apiInstance.saveBanque(
    banqueRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **banqueRequest** | **BanqueRequest**|  | |


### Return type

**BanqueDTO**

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

# **viewBanques**
> string viewBanques()


### Example

```typescript
import {
    BanqueRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new BanqueRestControllerApi(configuration);

const { status, data } = await apiInstance.viewBanques();
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

