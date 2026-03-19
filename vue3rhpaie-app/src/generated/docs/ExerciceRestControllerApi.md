# ExerciceRestControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**cloturerExercice**](#cloturerexercice) | **POST** /api/parametrages/exercices/cloturer | |
|[**getExerciceARecuperer**](#getexercicearecuperer) | **GET** /api/parametrages/exercices/a-recuperer | |
|[**getExerciceActif**](#getexerciceactif) | **GET** /api/parametrages/exercices/actif | |
|[**getExerciceList**](#getexercicelist) | **GET** /api/parametrages/exercices/list | |
|[**getPeriodeActive3**](#getperiodeactive3) | **GET** /api/parametrages/exercices/periode-active | |
|[**getSocietes7**](#getsocietes7) | **GET** /api/parametrages/exercices/societes | |
|[**viewExercice**](#viewexercice) | **GET** /api/parametrages/exercices/view | |

# **cloturerExercice**
> ExerciceDTO cloturerExercice(exerciceRequest)


### Example

```typescript
import {
    ExerciceRestControllerApi,
    Configuration,
    ExerciceRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new ExerciceRestControllerApi(configuration);

let exerciceRequest: ExerciceRequest; //

const { status, data } = await apiInstance.cloturerExercice(
    exerciceRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **exerciceRequest** | **ExerciceRequest**|  | |


### Return type

**ExerciceDTO**

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

# **getExerciceARecuperer**
> Array<Exercice> getExerciceARecuperer()


### Example

```typescript
import {
    ExerciceRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ExerciceRestControllerApi(configuration);

const { status, data } = await apiInstance.getExerciceARecuperer();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<Exercice>**

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

# **getExerciceActif**
> ExerciceDTO getExerciceActif()


### Example

```typescript
import {
    ExerciceRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ExerciceRestControllerApi(configuration);

const { status, data } = await apiInstance.getExerciceActif();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**ExerciceDTO**

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

# **getExerciceList**
> ExerciceResponseObject getExerciceList()


### Example

```typescript
import {
    ExerciceRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ExerciceRestControllerApi(configuration);

let limit: number; // (optional) (default to 10)
let offset: number; // (optional) (default to 0)
let search: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getExerciceList(
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

**ExerciceResponseObject**

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

# **getPeriodeActive3**
> PeriodePaie getPeriodeActive3()


### Example

```typescript
import {
    ExerciceRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ExerciceRestControllerApi(configuration);

const { status, data } = await apiInstance.getPeriodeActive3();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**PeriodePaie**

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

# **getSocietes7**
> Array<Societe> getSocietes7()


### Example

```typescript
import {
    ExerciceRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ExerciceRestControllerApi(configuration);

const { status, data } = await apiInstance.getSocietes7();
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

# **viewExercice**
> string viewExercice()


### Example

```typescript
import {
    ExerciceRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ExerciceRestControllerApi(configuration);

const { status, data } = await apiInstance.viewExercice();
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

