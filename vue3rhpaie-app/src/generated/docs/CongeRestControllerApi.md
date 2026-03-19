# CongeRestControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**chargerCongeParPeriode**](#chargercongeparperiode) | **POST** /api/conge/livre-paie/periode | |
|[**genererBulletinConge**](#genererbulletinconge) | **POST** /api/conge/generer-bulletin | |
|[**getBulletinConge**](#getbulletinconge) | **GET** /api/conge/bulletin/{id} | |
|[**getProvisionCongeList**](#getprovisioncongelist) | **GET** /api/conge/provision/list | |

# **chargerCongeParPeriode**
> Array<Conge> chargerCongeParPeriode(congeRequest)


### Example

```typescript
import {
    CongeRestControllerApi,
    Configuration,
    CongeRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new CongeRestControllerApi(configuration);

let congeRequest: CongeRequest; //

const { status, data } = await apiInstance.chargerCongeParPeriode(
    congeRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **congeRequest** | **CongeRequest**|  | |


### Return type

**Array<Conge>**

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

# **genererBulletinConge**
> CongeDTO genererBulletinConge(congeRequest)


### Example

```typescript
import {
    CongeRestControllerApi,
    Configuration,
    CongeRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new CongeRestControllerApi(configuration);

let congeRequest: CongeRequest; //

const { status, data } = await apiInstance.genererBulletinConge(
    congeRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **congeRequest** | **CongeRequest**|  | |


### Return type

**CongeDTO**

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

# **getBulletinConge**
> Conge getBulletinConge()


### Example

```typescript
import {
    CongeRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new CongeRestControllerApi(configuration);

let id: number; // (default to undefined)

const { status, data } = await apiInstance.getBulletinConge(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | defaults to undefined|


### Return type

**Conge**

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

# **getProvisionCongeList**
> CongeResponseObject getProvisionCongeList()


### Example

```typescript
import {
    CongeRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new CongeRestControllerApi(configuration);

let limit: number; // (optional) (default to 10)
let offset: number; // (optional) (default to 0)
let search: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getProvisionCongeList(
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

**CongeResponseObject**

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

