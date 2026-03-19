# RubriqueRestControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deleteRubrique**](#deleterubrique) | **POST** /api/parametrages/rubriques/supprimer | |
|[**findRubrique**](#findrubrique) | **POST** /api/parametrages/rubriques/trouver | |
|[**findRubriques**](#findrubriques) | **POST** /api/parametrages/rubriques/lister | |
|[**getRubriqueList**](#getrubriquelist) | **GET** /api/parametrages/rubriques/list | |
|[**getSocietes4**](#getsocietes4) | **GET** /api/parametrages/rubriques/societes | |
|[**saveRubrique**](#saverubrique) | **POST** /api/parametrages/rubriques/enregistrer | |
|[**viewRubriques**](#viewrubriques) | **GET** /api/parametrages/rubriques/view | |

# **deleteRubrique**
> RubriqueDTO deleteRubrique(rubriqueRequest)


### Example

```typescript
import {
    RubriqueRestControllerApi,
    Configuration,
    RubriqueRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new RubriqueRestControllerApi(configuration);

let rubriqueRequest: RubriqueRequest; //

const { status, data } = await apiInstance.deleteRubrique(
    rubriqueRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **rubriqueRequest** | **RubriqueRequest**|  | |


### Return type

**RubriqueDTO**

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

# **findRubrique**
> RubriqueDTO findRubrique(rubriqueRequest)


### Example

```typescript
import {
    RubriqueRestControllerApi,
    Configuration,
    RubriqueRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new RubriqueRestControllerApi(configuration);

let rubriqueRequest: RubriqueRequest; //

const { status, data } = await apiInstance.findRubrique(
    rubriqueRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **rubriqueRequest** | **RubriqueRequest**|  | |


### Return type

**RubriqueDTO**

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

# **findRubriques**
> RubriqueDTO findRubriques()


### Example

```typescript
import {
    RubriqueRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new RubriqueRestControllerApi(configuration);

const { status, data } = await apiInstance.findRubriques();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**RubriqueDTO**

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

# **getRubriqueList**
> RubriqueResponseObject getRubriqueList()


### Example

```typescript
import {
    RubriqueRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new RubriqueRestControllerApi(configuration);

let limit: number; // (optional) (default to 10)
let offset: number; // (optional) (default to 0)
let search: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getRubriqueList(
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

**RubriqueResponseObject**

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

# **getSocietes4**
> Array<Societe> getSocietes4()


### Example

```typescript
import {
    RubriqueRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new RubriqueRestControllerApi(configuration);

const { status, data } = await apiInstance.getSocietes4();
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

# **saveRubrique**
> RubriqueDTO saveRubrique(rubriqueRequest)


### Example

```typescript
import {
    RubriqueRestControllerApi,
    Configuration,
    RubriqueRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new RubriqueRestControllerApi(configuration);

let rubriqueRequest: RubriqueRequest; //

const { status, data } = await apiInstance.saveRubrique(
    rubriqueRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **rubriqueRequest** | **RubriqueRequest**|  | |


### Return type

**RubriqueDTO**

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

# **viewRubriques**
> string viewRubriques()


### Example

```typescript
import {
    RubriqueRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new RubriqueRestControllerApi(configuration);

const { status, data } = await apiInstance.viewRubriques();
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

