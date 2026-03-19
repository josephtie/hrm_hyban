# TempEffectifRestControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**chercherTempEffectif**](#cherchertempeffectif) | **GET** /api/paie/temp-effectif/chercher | |
|[**getPeriodes**](#getperiodes) | **GET** /api/paie/temp-effectif/periodes | |
|[**getTempEffectifList**](#gettempeffectiflist) | **GET** /api/paie/temp-effectif/list | |
|[**saveTempEffectif**](#savetempeffectif) | **POST** /api/paie/temp-effectif/enregistrer | |

# **chercherTempEffectif**
> TempEffectif chercherTempEffectif()


### Example

```typescript
import {
    TempEffectifRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TempEffectifRestControllerApi(configuration);

let idPersonnel: number; // (default to undefined)
let idPeriodDep: number; // (default to undefined)

const { status, data } = await apiInstance.chercherTempEffectif(
    idPersonnel,
    idPeriodDep
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idPersonnel** | [**number**] |  | defaults to undefined|
| **idPeriodDep** | [**number**] |  | defaults to undefined|


### Return type

**TempEffectif**

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

# **getPeriodes**
> Array<PeriodePaie> getPeriodes()


### Example

```typescript
import {
    TempEffectifRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TempEffectifRestControllerApi(configuration);

const { status, data } = await apiInstance.getPeriodes();
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

# **getTempEffectifList**
> TempEffectifResponseTempEffectif getTempEffectifList()


### Example

```typescript
import {
    TempEffectifRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TempEffectifRestControllerApi(configuration);

let limit: number; // (optional) (default to 10)
let offset: number; // (optional) (default to 0)
let search: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getTempEffectifList(
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

**TempEffectifResponseTempEffectif**

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

# **saveTempEffectif**
> TempEffectifDTO saveTempEffectif(tempEffectifRequest)


### Example

```typescript
import {
    TempEffectifRestControllerApi,
    Configuration,
    TempEffectifRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new TempEffectifRestControllerApi(configuration);

let tempEffectifRequest: TempEffectifRequest; //

const { status, data } = await apiInstance.saveTempEffectif(
    tempEffectifRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **tempEffectifRequest** | **TempEffectifRequest**|  | |


### Return type

**TempEffectifDTO**

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

