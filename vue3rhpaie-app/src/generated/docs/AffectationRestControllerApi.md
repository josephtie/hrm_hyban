# AffectationRestControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deleteAffectation**](#deleteaffectation) | **POST** /api/rh/carriere/affectations/supprimer | |
|[**findAffectation**](#findaffectation) | **POST** /api/rh/carriere/affectations/trouver | |
|[**findAffectations**](#findaffectations) | **POST** /api/rh/carriere/affectations/lister | |
|[**findAffectationsByPersonnel**](#findaffectationsbypersonnel) | **POST** /api/rh/carriere/affectations/lister-par-personnel | |
|[**findAffectationsByPoste**](#findaffectationsbyposte) | **POST** /api/rh/carriere/affectations/lister-par-poste | |
|[**getAffectationList**](#getaffectationlist) | **GET** /api/rh/carriere/affectations/list | |
|[**getPeriodeActive**](#getperiodeactive) | **GET** /api/rh/carriere/affectations/periode-active | |
|[**saveAffectation**](#saveaffectation) | **POST** /api/rh/carriere/affectations/enregistrer | |
|[**viewAffectations**](#viewaffectations) | **GET** /api/rh/carriere/affectations/view | |

# **deleteAffectation**
> AffectationDTO deleteAffectation(affectationRequest)


### Example

```typescript
import {
    AffectationRestControllerApi,
    Configuration,
    AffectationRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new AffectationRestControllerApi(configuration);

let affectationRequest: AffectationRequest; //

const { status, data } = await apiInstance.deleteAffectation(
    affectationRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **affectationRequest** | **AffectationRequest**|  | |


### Return type

**AffectationDTO**

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

# **findAffectation**
> AffectationDTO findAffectation(affectationRequest)


### Example

```typescript
import {
    AffectationRestControllerApi,
    Configuration,
    AffectationRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new AffectationRestControllerApi(configuration);

let affectationRequest: AffectationRequest; //

const { status, data } = await apiInstance.findAffectation(
    affectationRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **affectationRequest** | **AffectationRequest**|  | |


### Return type

**AffectationDTO**

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

# **findAffectations**
> AffectationDTO findAffectations()


### Example

```typescript
import {
    AffectationRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AffectationRestControllerApi(configuration);

const { status, data } = await apiInstance.findAffectations();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**AffectationDTO**

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

# **findAffectationsByPersonnel**
> AffectationDTO findAffectationsByPersonnel(affectationRequest)


### Example

```typescript
import {
    AffectationRestControllerApi,
    Configuration,
    AffectationRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new AffectationRestControllerApi(configuration);

let affectationRequest: AffectationRequest; //

const { status, data } = await apiInstance.findAffectationsByPersonnel(
    affectationRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **affectationRequest** | **AffectationRequest**|  | |


### Return type

**AffectationDTO**

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

# **findAffectationsByPoste**
> AffectationDTO findAffectationsByPoste(affectationRequest)


### Example

```typescript
import {
    AffectationRestControllerApi,
    Configuration,
    AffectationRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new AffectationRestControllerApi(configuration);

let affectationRequest: AffectationRequest; //

const { status, data } = await apiInstance.findAffectationsByPoste(
    affectationRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **affectationRequest** | **AffectationRequest**|  | |


### Return type

**AffectationDTO**

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

# **getAffectationList**
> AffectationResponseObject getAffectationList()


### Example

```typescript
import {
    AffectationRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AffectationRestControllerApi(configuration);

let limit: number; // (optional) (default to 10)
let offset: number; // (optional) (default to 0)
let search: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getAffectationList(
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

**AffectationResponseObject**

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

# **getPeriodeActive**
> PeriodePaie getPeriodeActive()


### Example

```typescript
import {
    AffectationRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AffectationRestControllerApi(configuration);

const { status, data } = await apiInstance.getPeriodeActive();
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

# **saveAffectation**
> AffectationDTO saveAffectation(affectationRequest)


### Example

```typescript
import {
    AffectationRestControllerApi,
    Configuration,
    AffectationRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new AffectationRestControllerApi(configuration);

let affectationRequest: AffectationRequest; //

const { status, data } = await apiInstance.saveAffectation(
    affectationRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **affectationRequest** | **AffectationRequest**|  | |


### Return type

**AffectationDTO**

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

# **viewAffectations**
> string viewAffectations()


### Example

```typescript
import {
    AffectationRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AffectationRestControllerApi(configuration);

const { status, data } = await apiInstance.viewAffectations();
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

