# EchelonnementRestControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**getEchelonnementDuPret**](#getechelonnementdupret) | **GET** /api/paie/echelonnement/pret/{idpretperso} | |
|[**getEchelonnementList**](#getechelonnementlist) | **GET** /api/paie/echelonnement/list | |
|[**getPeriodes4**](#getperiodes4) | **GET** /api/paie/echelonnement/periodes | |
|[**updateEchelonnement**](#updateechelonnement) | **POST** /api/paie/echelonnement/update | |

# **getEchelonnementDuPret**
> EchelonnementDTO getEchelonnementDuPret()


### Example

```typescript
import {
    EchelonnementRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new EchelonnementRestControllerApi(configuration);

let idpretperso: number; // (default to undefined)

const { status, data } = await apiInstance.getEchelonnementDuPret(
    idpretperso
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idpretperso** | [**number**] |  | defaults to undefined|


### Return type

**EchelonnementDTO**

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

# **getEchelonnementList**
> EchelonnementResponseObject getEchelonnementList()


### Example

```typescript
import {
    EchelonnementRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new EchelonnementRestControllerApi(configuration);

let limit: number; // (optional) (default to 10)
let offset: number; // (optional) (default to 0)
let search: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getEchelonnementList(
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

**EchelonnementResponseObject**

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

# **getPeriodes4**
> Array<PeriodePaie> getPeriodes4()


### Example

```typescript
import {
    EchelonnementRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new EchelonnementRestControllerApi(configuration);

const { status, data } = await apiInstance.getPeriodes4();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<PeriodePaie>**

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

# **updateEchelonnement**
> EchelonnementResponseObject updateEchelonnement(echelonnementRequest)


### Example

```typescript
import {
    EchelonnementRestControllerApi,
    Configuration,
    EchelonnementRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new EchelonnementRestControllerApi(configuration);

let echelonnementRequest: EchelonnementRequest; //

const { status, data } = await apiInstance.updateEchelonnement(
    echelonnementRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **echelonnementRequest** | **EchelonnementRequest**|  | |


### Return type

**EchelonnementResponseObject**

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

