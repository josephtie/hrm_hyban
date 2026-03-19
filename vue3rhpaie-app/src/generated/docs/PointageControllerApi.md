# PointageControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deletePointage**](#deletepointage) | **POST** /api/personnel/supprimerpointage | |
|[**findPointage**](#findpointage) | **POST** /api/personnel/trouverpointage | |
|[**findPointagePersonneperiode**](#findpointagepersonneperiode) | **POST** /api/personnel/trouverpointagePersonneparperiode | |
|[**findPointageperiode**](#findpointageperiode) | **POST** /api/personnel/trouverpointageparperiode | |
|[**findPointages**](#findpointages) | **POST** /api/personnel/listerpointages | |
|[**findPointagesByPersonnelsAndDate1**](#findpointagesbypersonnelsanddate1) | **POST** /api/personnel/listerpointagesparlistpersonneletdate | |
|[**getUserListJSON5**](#getuserlistjson5) | **POST** /api/personnel/paginerpointages | |
|[**initPointage**](#initpointage) | **GET** /api/personnel/pointages/init | |
|[**savePointage**](#savepointage) | **POST** /api/personnel/enregistrerpointage | |

# **deletePointage**
> PointageDTO deletePointage(idRequest)


### Example

```typescript
import {
    PointageControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PointageControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.deletePointage(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**PointageDTO**

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

# **findPointage**
> PointageDTO findPointage(idRequest)


### Example

```typescript
import {
    PointageControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PointageControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.findPointage(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**PointageDTO**

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

# **findPointagePersonneperiode**
> PointageDTO findPointagePersonneperiode(pointageSearchRequest)


### Example

```typescript
import {
    PointageControllerApi,
    Configuration,
    PointageSearchRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PointageControllerApi(configuration);

let pointageSearchRequest: PointageSearchRequest; //

const { status, data } = await apiInstance.findPointagePersonneperiode(
    pointageSearchRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **pointageSearchRequest** | **PointageSearchRequest**|  | |


### Return type

**PointageDTO**

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

# **findPointageperiode**
> PointageDTO findPointageperiode(pointageSearchRequest)


### Example

```typescript
import {
    PointageControllerApi,
    Configuration,
    PointageSearchRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PointageControllerApi(configuration);

let pointageSearchRequest: PointageSearchRequest; //

const { status, data } = await apiInstance.findPointageperiode(
    pointageSearchRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **pointageSearchRequest** | **PointageSearchRequest**|  | |


### Return type

**PointageDTO**

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

# **findPointages**
> PointageDTO findPointages()


### Example

```typescript
import {
    PointageControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PointageControllerApi(configuration);

const { status, data } = await apiInstance.findPointages();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**PointageDTO**

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

# **findPointagesByPersonnelsAndDate1**
> PointageDTO findPointagesByPersonnelsAndDate1(pointageSearchRequest)


### Example

```typescript
import {
    PointageControllerApi,
    Configuration,
    PointageSearchRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PointageControllerApi(configuration);

let pointageSearchRequest: PointageSearchRequest; //

const { status, data } = await apiInstance.findPointagesByPersonnelsAndDate1(
    pointageSearchRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **pointageSearchRequest** | **PointageSearchRequest**|  | |


### Return type

**PointageDTO**

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

# **getUserListJSON5**
> PointageDTO getUserListJSON5(paginationRequest)


### Example

```typescript
import {
    PointageControllerApi,
    Configuration,
    PaginationRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PointageControllerApi(configuration);

let paginationRequest: PaginationRequest; //

const { status, data } = await apiInstance.getUserListJSON5(
    paginationRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **paginationRequest** | **PaginationRequest**|  | |


### Return type

**PointageDTO**

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

# **initPointage**
> { [key: string]: object; } initPointage()


### Example

```typescript
import {
    PointageControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PointageControllerApi(configuration);

const { status, data } = await apiInstance.initPointage();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**{ [key: string]: object; }**

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

# **savePointage**
> PointageDTO savePointage(pointageRequest)


### Example

```typescript
import {
    PointageControllerApi,
    Configuration,
    PointageRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PointageControllerApi(configuration);

let pointageRequest: PointageRequest; //

const { status, data } = await apiInstance.savePointage(
    pointageRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **pointageRequest** | **PointageRequest**|  | |


### Return type

**PointageDTO**

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

