# AbsencesRestControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deleteAbsences**](#deleteabsences) | **POST** /api/rh/absences/supprimer | |
|[**findAbsences**](#findabsences) | **POST** /api/rh/absences/trouver | |
|[**findAbsences1**](#findabsences1) | **POST** /api/rh/absences/lister | |
|[**getAbsencesList**](#getabsenceslist) | **GET** /api/rh/absences/list | |
|[**getSocietes3**](#getsocietes3) | **GET** /api/rh/absences/societes | |
|[**saveAbsences**](#saveabsences) | **POST** /api/rh/absences/enregistrer | |
|[**viewAbsences**](#viewabsences) | **GET** /api/rh/absences/view | |

# **deleteAbsences**
> AbsencesDTO deleteAbsences(absencesRequest)


### Example

```typescript
import {
    AbsencesRestControllerApi,
    Configuration,
    AbsencesRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new AbsencesRestControllerApi(configuration);

let absencesRequest: AbsencesRequest; //

const { status, data } = await apiInstance.deleteAbsences(
    absencesRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **absencesRequest** | **AbsencesRequest**|  | |


### Return type

**AbsencesDTO**

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

# **findAbsences**
> AbsencesDTO findAbsences(absencesRequest)


### Example

```typescript
import {
    AbsencesRestControllerApi,
    Configuration,
    AbsencesRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new AbsencesRestControllerApi(configuration);

let absencesRequest: AbsencesRequest; //

const { status, data } = await apiInstance.findAbsences(
    absencesRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **absencesRequest** | **AbsencesRequest**|  | |


### Return type

**AbsencesDTO**

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

# **findAbsences1**
> AbsencesDTO findAbsences1()


### Example

```typescript
import {
    AbsencesRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AbsencesRestControllerApi(configuration);

const { status, data } = await apiInstance.findAbsences1();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**AbsencesDTO**

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

# **getAbsencesList**
> AbsencesResponseObject getAbsencesList()


### Example

```typescript
import {
    AbsencesRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AbsencesRestControllerApi(configuration);

let limit: number; // (optional) (default to 10)
let offset: number; // (optional) (default to 0)
let search: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getAbsencesList(
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

**AbsencesResponseObject**

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

# **getSocietes3**
> Array<Societe> getSocietes3()


### Example

```typescript
import {
    AbsencesRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AbsencesRestControllerApi(configuration);

const { status, data } = await apiInstance.getSocietes3();
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

# **saveAbsences**
> AbsencesDTO saveAbsences(absencesRequest)


### Example

```typescript
import {
    AbsencesRestControllerApi,
    Configuration,
    AbsencesRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new AbsencesRestControllerApi(configuration);

let absencesRequest: AbsencesRequest; //

const { status, data } = await apiInstance.saveAbsences(
    absencesRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **absencesRequest** | **AbsencesRequest**|  | |


### Return type

**AbsencesDTO**

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

# **viewAbsences**
> string viewAbsences()


### Example

```typescript
import {
    AbsencesRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AbsencesRestControllerApi(configuration);

const { status, data } = await apiInstance.viewAbsences();
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

