# PeriodePaieRestControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**cloturerPeriode**](#cloturerperiode) | **POST** /api/parametrages/periodes/cloturer | |
|[**getPeriodeActive1**](#getperiodeactive1) | **GET** /api/parametrages/periodes/active | |
|[**getPeriodeList**](#getperiodelist) | **GET** /api/parametrages/periodes/list | |
|[**getSocietes5**](#getsocietes5) | **GET** /api/parametrages/periodes/societes | |
|[**listPeriodeAll**](#listperiodeall) | **POST** /api/parametrages/periodes/all | |
|[**listPeriodeCloturee**](#listperiodecloturee) | **POST** /api/parametrages/periodes/cloturees | |
|[**listPeriodeOuverte**](#listperiodeouverte) | **POST** /api/parametrages/periodes/ouvertes | |
|[**savePeriode**](#saveperiode) | **POST** /api/parametrages/periodes/save | |
|[**viewPeriode**](#viewperiode) | **GET** /api/parametrages/periodes/view | |

# **cloturerPeriode**
> PeriodePaieDTO cloturerPeriode(periodePaieRequest)


### Example

```typescript
import {
    PeriodePaieRestControllerApi,
    Configuration,
    PeriodePaieRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PeriodePaieRestControllerApi(configuration);

let periodePaieRequest: PeriodePaieRequest; //

const { status, data } = await apiInstance.cloturerPeriode(
    periodePaieRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **periodePaieRequest** | **PeriodePaieRequest**|  | |


### Return type

**PeriodePaieDTO**

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

# **getPeriodeActive1**
> PeriodePaieDTO getPeriodeActive1()


### Example

```typescript
import {
    PeriodePaieRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PeriodePaieRestControllerApi(configuration);

const { status, data } = await apiInstance.getPeriodeActive1();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**PeriodePaieDTO**

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

# **getPeriodeList**
> PeriodePaieResponseObject getPeriodeList()


### Example

```typescript
import {
    PeriodePaieRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PeriodePaieRestControllerApi(configuration);

let limit: number; // (optional) (default to 10)
let offset: number; // (optional) (default to 0)
let search: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getPeriodeList(
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

**PeriodePaieResponseObject**

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

# **getSocietes5**
> Array<Societe> getSocietes5()


### Example

```typescript
import {
    PeriodePaieRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PeriodePaieRestControllerApi(configuration);

const { status, data } = await apiInstance.getSocietes5();
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

# **listPeriodeAll**
> Array<PeriodePaie> listPeriodeAll()


### Example

```typescript
import {
    PeriodePaieRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PeriodePaieRestControllerApi(configuration);

const { status, data } = await apiInstance.listPeriodeAll();
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

# **listPeriodeCloturee**
> Array<PeriodePaie> listPeriodeCloturee()


### Example

```typescript
import {
    PeriodePaieRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PeriodePaieRestControllerApi(configuration);

const { status, data } = await apiInstance.listPeriodeCloturee();
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

# **listPeriodeOuverte**
> Array<PeriodePaie> listPeriodeOuverte()


### Example

```typescript
import {
    PeriodePaieRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PeriodePaieRestControllerApi(configuration);

const { status, data } = await apiInstance.listPeriodeOuverte();
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

# **savePeriode**
> PeriodePaieDTO savePeriode(periodePaieRequest)


### Example

```typescript
import {
    PeriodePaieRestControllerApi,
    Configuration,
    PeriodePaieRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PeriodePaieRestControllerApi(configuration);

let periodePaieRequest: PeriodePaieRequest; //

const { status, data } = await apiInstance.savePeriode(
    periodePaieRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **periodePaieRequest** | **PeriodePaieRequest**|  | |


### Return type

**PeriodePaieDTO**

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

# **viewPeriode**
> string viewPeriode()


### Example

```typescript
import {
    PeriodePaieRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PeriodePaieRestControllerApi(configuration);

const { status, data } = await apiInstance.viewPeriode();
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

