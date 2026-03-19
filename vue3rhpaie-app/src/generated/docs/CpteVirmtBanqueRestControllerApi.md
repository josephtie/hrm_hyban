# CpteVirmtBanqueRestControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deleteCpteVirmtBanque**](#deletecptevirmtbanque) | **POST** /api/parametrages/cpte-virement/supprimer | |
|[**findCpteVirmtBanque**](#findcptevirmtbanque) | **POST** /api/parametrages/cpte-virement/trouver | |
|[**findCpteVirmtBanques**](#findcptevirmtbanques) | **POST** /api/parametrages/cpte-virement/lister | |
|[**getBanques**](#getbanques) | **GET** /api/parametrages/cpte-virement/banques | |
|[**getCpteVirmtBanqueList**](#getcptevirmtbanquelist) | **GET** /api/parametrages/cpte-virement/list | |
|[**getSocietes8**](#getsocietes8) | **GET** /api/parametrages/cpte-virement/societes | |
|[**saveCpteVirmtBanque**](#savecptevirmtbanque) | **POST** /api/parametrages/cpte-virement/enregistrer | |
|[**viewCpteVirement**](#viewcptevirement) | **GET** /api/parametrages/cpte-virement/view | |

# **deleteCpteVirmtBanque**
> CpteVirmtBanqueDTO deleteCpteVirmtBanque(cpteVirmtBanqueRequest)


### Example

```typescript
import {
    CpteVirmtBanqueRestControllerApi,
    Configuration,
    CpteVirmtBanqueRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new CpteVirmtBanqueRestControllerApi(configuration);

let cpteVirmtBanqueRequest: CpteVirmtBanqueRequest; //

const { status, data } = await apiInstance.deleteCpteVirmtBanque(
    cpteVirmtBanqueRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **cpteVirmtBanqueRequest** | **CpteVirmtBanqueRequest**|  | |


### Return type

**CpteVirmtBanqueDTO**

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

# **findCpteVirmtBanque**
> CpteVirmtBanqueDTO findCpteVirmtBanque(cpteVirmtBanqueRequest)


### Example

```typescript
import {
    CpteVirmtBanqueRestControllerApi,
    Configuration,
    CpteVirmtBanqueRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new CpteVirmtBanqueRestControllerApi(configuration);

let cpteVirmtBanqueRequest: CpteVirmtBanqueRequest; //

const { status, data } = await apiInstance.findCpteVirmtBanque(
    cpteVirmtBanqueRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **cpteVirmtBanqueRequest** | **CpteVirmtBanqueRequest**|  | |


### Return type

**CpteVirmtBanqueDTO**

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

# **findCpteVirmtBanques**
> CpteVirmtBanqueDTO findCpteVirmtBanques()


### Example

```typescript
import {
    CpteVirmtBanqueRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new CpteVirmtBanqueRestControllerApi(configuration);

const { status, data } = await apiInstance.findCpteVirmtBanques();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**CpteVirmtBanqueDTO**

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

# **getBanques**
> object getBanques()


### Example

```typescript
import {
    CpteVirmtBanqueRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new CpteVirmtBanqueRestControllerApi(configuration);

const { status, data } = await apiInstance.getBanques();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**object**

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

# **getCpteVirmtBanqueList**
> CpteVirmtBanqueResponseObject getCpteVirmtBanqueList()


### Example

```typescript
import {
    CpteVirmtBanqueRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new CpteVirmtBanqueRestControllerApi(configuration);

let limit: number; // (optional) (default to 10)
let offset: number; // (optional) (default to 0)
let search: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getCpteVirmtBanqueList(
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

**CpteVirmtBanqueResponseObject**

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

# **getSocietes8**
> Array<Societe> getSocietes8()


### Example

```typescript
import {
    CpteVirmtBanqueRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new CpteVirmtBanqueRestControllerApi(configuration);

const { status, data } = await apiInstance.getSocietes8();
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

# **saveCpteVirmtBanque**
> CpteVirmtBanqueDTO saveCpteVirmtBanque(cpteVirmtBanqueRequest)


### Example

```typescript
import {
    CpteVirmtBanqueRestControllerApi,
    Configuration,
    CpteVirmtBanqueRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new CpteVirmtBanqueRestControllerApi(configuration);

let cpteVirmtBanqueRequest: CpteVirmtBanqueRequest; //

const { status, data } = await apiInstance.saveCpteVirmtBanque(
    cpteVirmtBanqueRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **cpteVirmtBanqueRequest** | **CpteVirmtBanqueRequest**|  | |


### Return type

**CpteVirmtBanqueDTO**

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

# **viewCpteVirement**
> string viewCpteVirement()


### Example

```typescript
import {
    CpteVirmtBanqueRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new CpteVirmtBanqueRestControllerApi(configuration);

const { status, data } = await apiInstance.viewCpteVirement();
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

