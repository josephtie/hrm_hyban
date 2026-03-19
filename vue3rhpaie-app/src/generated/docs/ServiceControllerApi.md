# ServiceControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deleteService**](#deleteservice) | **POST** /api/personnels/supprimerservice | |
|[**getDepartementByDirection**](#getdepartementbydirection) | **POST** /api/personnels/listdepartementbydirection | |
|[**getServiceListJSON**](#getservicelistjson) | **POST** /api/personnels/listservicejson | |
|[**listeServiceParType**](#listeservicepartype) | **POST** /api/personnels/listservicepartype | |
|[**saveService**](#saveservice) | **POST** /api/personnels/enregisterservice | |
|[**viewService**](#viewservice) | **GET** /api/personnels/services/init | |

# **deleteService**
> ServiceDTO deleteService(idRequest)


### Example

```typescript
import {
    ServiceControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new ServiceControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.deleteService(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**ServiceDTO**

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

# **getDepartementByDirection**
> Array<Service> getDepartementByDirection(idRequest)


### Example

```typescript
import {
    ServiceControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new ServiceControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.getDepartementByDirection(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**Array<Service>**

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

# **getServiceListJSON**
> ServiceDTO getServiceListJSON(paginationRequest)


### Example

```typescript
import {
    ServiceControllerApi,
    Configuration,
    PaginationRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new ServiceControllerApi(configuration);

let paginationRequest: PaginationRequest; //

const { status, data } = await apiInstance.getServiceListJSON(
    paginationRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **paginationRequest** | **PaginationRequest**|  | |


### Return type

**ServiceDTO**

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

# **listeServiceParType**
> Array<Service> listeServiceParType(idRequest)


### Example

```typescript
import {
    ServiceControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new ServiceControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.listeServiceParType(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**Array<Service>**

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

# **saveService**
> ServiceDTO saveService(serviceRequest)


### Example

```typescript
import {
    ServiceControllerApi,
    Configuration,
    ServiceRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new ServiceControllerApi(configuration);

let serviceRequest: ServiceRequest; //

const { status, data } = await apiInstance.saveService(
    serviceRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **serviceRequest** | **ServiceRequest**|  | |


### Return type

**ServiceDTO**

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

# **viewService**
> object viewService()


### Example

```typescript
import {
    ServiceControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ServiceControllerApi(configuration);

const { status, data } = await apiInstance.viewService();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**object**

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

