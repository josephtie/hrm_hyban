# MoisRestControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**getMoisList**](#getmoislist) | **GET** /api/parametrages/mois/list | |
|[**getPeriodeActive2**](#getperiodeactive2) | **GET** /api/parametrages/mois/periode-active | |
|[**getSocietes6**](#getsocietes6) | **GET** /api/parametrages/mois/societes | |
|[**listMois**](#listmois) | **GET** /api/parametrages/mois/list-all | |
|[**viewMois**](#viewmois) | **GET** /api/parametrages/mois/view | |

# **getMoisList**
> MoisResponseObject getMoisList()


### Example

```typescript
import {
    MoisRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new MoisRestControllerApi(configuration);

let limit: number; // (optional) (default to 10)
let offset: number; // (optional) (default to 0)
let search: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getMoisList(
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

**MoisResponseObject**

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

# **getPeriodeActive2**
> PeriodePaie getPeriodeActive2()


### Example

```typescript
import {
    MoisRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new MoisRestControllerApi(configuration);

const { status, data } = await apiInstance.getPeriodeActive2();
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

# **getSocietes6**
> Array<Societe> getSocietes6()


### Example

```typescript
import {
    MoisRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new MoisRestControllerApi(configuration);

const { status, data } = await apiInstance.getSocietes6();
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

# **listMois**
> MoisDTO listMois()


### Example

```typescript
import {
    MoisRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new MoisRestControllerApi(configuration);

const { status, data } = await apiInstance.listMois();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**MoisDTO**

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

# **viewMois**
> string viewMois()


### Example

```typescript
import {
    MoisRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new MoisRestControllerApi(configuration);

const { status, data } = await apiInstance.viewMois();
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

