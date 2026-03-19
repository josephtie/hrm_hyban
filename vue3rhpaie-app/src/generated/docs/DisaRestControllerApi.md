# DisaRestControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**genererEtat3**](#genereretat3) | **GET** /api/paie/disa/etat3 | |
|[**genererEtat301**](#genereretat301) | **GET** /api/paie/disa/etat301 | |
|[**genererIts**](#genererits) | **GET** /api/paie/disa/its | |
|[**getExercices**](#getexercices) | **GET** /api/paie/disa/exercices | |
|[**getPeriodes5**](#getperiodes5) | **GET** /api/paie/disa/periodes | |

# **genererEtat3**
> string genererEtat3()


### Example

```typescript
import {
    DisaRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new DisaRestControllerApi(configuration);

let annee: number; // (default to undefined)
let print: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.genererEtat3(
    annee,
    print
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **annee** | [**number**] |  | defaults to undefined|
| **print** | [**string**] |  | (optional) defaults to undefined|


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

# **genererEtat301**
> string genererEtat301()


### Example

```typescript
import {
    DisaRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new DisaRestControllerApi(configuration);

let annee: number; // (default to undefined)
let print: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.genererEtat301(
    annee,
    print
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **annee** | [**number**] |  | defaults to undefined|
| **print** | [**string**] |  | (optional) defaults to undefined|


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

# **genererIts**
> string genererIts()


### Example

```typescript
import {
    DisaRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new DisaRestControllerApi(configuration);

let periode: number; // (default to undefined)
let print: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.genererIts(
    periode,
    print
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **periode** | [**number**] |  | defaults to undefined|
| **print** | [**string**] |  | (optional) defaults to undefined|


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

# **getExercices**
> Array<Exercice> getExercices()


### Example

```typescript
import {
    DisaRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new DisaRestControllerApi(configuration);

const { status, data } = await apiInstance.getExercices();
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

# **getPeriodes5**
> Array<PeriodePaie> getPeriodes5()


### Example

```typescript
import {
    DisaRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new DisaRestControllerApi(configuration);

const { status, data } = await apiInstance.getPeriodes5();
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

