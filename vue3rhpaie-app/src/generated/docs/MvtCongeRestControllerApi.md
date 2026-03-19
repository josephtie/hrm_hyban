# MvtCongeRestControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deleteMvtConge**](#deletemvtconge) | **DELETE** /api/personnel/mvt-conges/supprimer/{id} | |
|[**findMvtConge**](#findmvtconge) | **GET** /api/personnel/mvt-conges/trouver/{id} | |
|[**getMvtCongeListJSON**](#getmvtcongelistjson) | **GET** /api/personnel/mvt-conges/paginer | |
|[**listerMvtConges**](#listermvtconges) | **GET** /api/personnel/mvt-conges/lister | |
|[**listerMvtCongesParPersonnel**](#listermvtcongesparpersonnel) | **GET** /api/personnel/mvt-conges/lister/personnel/{idPersonnel} | |
|[**saveMvtConge**](#savemvtconge) | **POST** /api/personnel/mvt-conges/enregistrer | |

# **deleteMvtConge**
> MvtCongeDTO deleteMvtConge()


### Example

```typescript
import {
    MvtCongeRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new MvtCongeRestControllerApi(configuration);

let id: number; // (default to undefined)

const { status, data } = await apiInstance.deleteMvtConge(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | defaults to undefined|


### Return type

**MvtCongeDTO**

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

# **findMvtConge**
> MvtCongeDTO findMvtConge()


### Example

```typescript
import {
    MvtCongeRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new MvtCongeRestControllerApi(configuration);

let id: number; // (default to undefined)

const { status, data } = await apiInstance.findMvtConge(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | defaults to undefined|


### Return type

**MvtCongeDTO**

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

# **getMvtCongeListJSON**
> MvtCongeResponseObject getMvtCongeListJSON()


### Example

```typescript
import {
    MvtCongeRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new MvtCongeRestControllerApi(configuration);

let limit: number; // (optional) (default to 10)
let offset: number; // (optional) (default to 0)
let search: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getMvtCongeListJSON(
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

**MvtCongeResponseObject**

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

# **listerMvtConges**
> MvtCongeDTO listerMvtConges()


### Example

```typescript
import {
    MvtCongeRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new MvtCongeRestControllerApi(configuration);

const { status, data } = await apiInstance.listerMvtConges();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**MvtCongeDTO**

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

# **listerMvtCongesParPersonnel**
> MvtCongeDTO listerMvtCongesParPersonnel()


### Example

```typescript
import {
    MvtCongeRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new MvtCongeRestControllerApi(configuration);

let idPersonnel: number; // (default to undefined)

const { status, data } = await apiInstance.listerMvtCongesParPersonnel(
    idPersonnel
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idPersonnel** | [**number**] |  | defaults to undefined|


### Return type

**MvtCongeDTO**

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

# **saveMvtConge**
> MvtCongeDTO saveMvtConge(mvtCongeRequest)


### Example

```typescript
import {
    MvtCongeRestControllerApi,
    Configuration,
    MvtCongeRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new MvtCongeRestControllerApi(configuration);

let mvtCongeRequest: MvtCongeRequest; //

const { status, data } = await apiInstance.saveMvtConge(
    mvtCongeRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **mvtCongeRequest** | **MvtCongeRequest**|  | |


### Return type

**MvtCongeDTO**

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

