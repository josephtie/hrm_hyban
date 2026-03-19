# RubriqVariableRestControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**chercherRubriqVariable**](#chercherrubriqvariable) | **GET** /api/paie/rubrique-variable/chercher/{idPersonnel} | |
|[**getPeriodes1**](#getperiodes1) | **GET** /api/paie/rubrique-variable/periodes | |
|[**getPersonnels**](#getpersonnels) | **GET** /api/paie/rubrique-variable/personnels | |
|[**getRubriqVariableList**](#getrubriqvariablelist) | **GET** /api/paie/rubrique-variable/list | |
|[**saveRubriqVariable**](#saverubriqvariable) | **POST** /api/paie/rubrique-variable/enregistrer | |

# **chercherRubriqVariable**
> RubriqVariable chercherRubriqVariable()


### Example

```typescript
import {
    RubriqVariableRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new RubriqVariableRestControllerApi(configuration);

let idPersonnel: number; // (default to undefined)

const { status, data } = await apiInstance.chercherRubriqVariable(
    idPersonnel
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idPersonnel** | [**number**] |  | defaults to undefined|


### Return type

**RubriqVariable**

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

# **getPeriodes1**
> Array<PeriodePaie> getPeriodes1()


### Example

```typescript
import {
    RubriqVariableRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new RubriqVariableRestControllerApi(configuration);

const { status, data } = await apiInstance.getPeriodes1();
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

# **getPersonnels**
> Array<Personnel> getPersonnels()


### Example

```typescript
import {
    RubriqVariableRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new RubriqVariableRestControllerApi(configuration);

const { status, data } = await apiInstance.getPersonnels();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<Personnel>**

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

# **getRubriqVariableList**
> RubriqVariableResponseObject getRubriqVariableList()


### Example

```typescript
import {
    RubriqVariableRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new RubriqVariableRestControllerApi(configuration);

let limit: number; // (optional) (default to 10)
let offset: number; // (optional) (default to 0)
let search: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getRubriqVariableList(
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

**RubriqVariableResponseObject**

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

# **saveRubriqVariable**
> RubriqVariableDTO saveRubriqVariable(rubriqVariableRequest)


### Example

```typescript
import {
    RubriqVariableRestControllerApi,
    Configuration,
    RubriqVariableRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new RubriqVariableRestControllerApi(configuration);

let rubriqVariableRequest: RubriqVariableRequest; //

const { status, data } = await apiInstance.saveRubriqVariable(
    rubriqVariableRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **rubriqVariableRequest** | **RubriqVariableRequest**|  | |


### Return type

**RubriqVariableDTO**

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

