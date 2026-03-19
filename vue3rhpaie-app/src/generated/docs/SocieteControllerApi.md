# SocieteControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deleteSociete**](#deletesociete) | **POST** /api/parametrages/societe/deletesociete | |
|[**getsocieteju**](#getsocieteju) | **POST** /api/parametrages/societe/societejson | |
|[**listMoisyy**](#listmoisyy) | **GET** /api/parametrages/societe/societlistmois | |
|[**saveSociete**](#savesociete) | **POST** /api/parametrages/societe/enregistrersociete | |
|[**selectSociete**](#selectsociete) | **POST** /api/parametrages/societe/choisirsociete | |
|[**uploadFile**](#uploadfile) | **POST** /api/parametrages/societe/saveattrib | |

# **deleteSociete**
> SocieteDTO deleteSociete(idRequest)


### Example

```typescript
import {
    SocieteControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new SocieteControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.deleteSociete(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**SocieteDTO**

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

# **getsocieteju**
> SocieteDTO getsocieteju(paginationRequest)


### Example

```typescript
import {
    SocieteControllerApi,
    Configuration,
    PaginationRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new SocieteControllerApi(configuration);

let paginationRequest: PaginationRequest; //

const { status, data } = await apiInstance.getsocieteju(
    paginationRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **paginationRequest** | **PaginationRequest**|  | |


### Return type

**SocieteDTO**

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

# **listMoisyy**
> MoisDTO listMoisyy()


### Example

```typescript
import {
    SocieteControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new SocieteControllerApi(configuration);

const { status, data } = await apiInstance.listMoisyy();
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

# **saveSociete**
> SocieteDTO saveSociete(societeRequest)


### Example

```typescript
import {
    SocieteControllerApi,
    Configuration,
    SocieteRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new SocieteControllerApi(configuration);

let societeRequest: SocieteRequest; //

const { status, data } = await apiInstance.saveSociete(
    societeRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **societeRequest** | **SocieteRequest**|  | |


### Return type

**SocieteDTO**

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

# **selectSociete**
> Societe selectSociete(idRequest)


### Example

```typescript
import {
    SocieteControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new SocieteControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.selectSociete(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**Societe**

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

# **uploadFile**
> SocieteDTO uploadFile()


### Example

```typescript
import {
    SocieteControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new SocieteControllerApi(configuration);

const { status, data } = await apiInstance.uploadFile();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**SocieteDTO**

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

