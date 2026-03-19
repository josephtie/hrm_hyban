# FormationPersonnelControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deletePoste**](#deleteposte) | **POST** /formation/supprimerformationpersonnel | |
|[**findFormationPersonnel**](#findformationpersonnel) | **POST** /formation/trouverformationpersonnel | |
|[**findPointagesByPersonnelsAndDate**](#findpointagesbypersonnelsanddate) | **POST** /formation/listerformationpersonnelsparlistpersonneletidformation | |
|[**findPostes1**](#findpostes1) | **POST** /formation/listerformationpersonnels | |
|[**getFormationPersonnelListJSON**](#getformationpersonnellistjson) | **POST** /formation/paginerformationpersonnels | |
|[**getUserListJSON1**](#getuserlistjson1) | **POST** /formation/paginerformationpersonnelsduneformation | |
|[**saveFormationPeronnel**](#saveformationperonnel) | **POST** /formation/enregistrerformationpersonnel | |
|[**savePointagesByPersonnelsAndDate**](#savepointagesbypersonnelsanddate) | **POST** /formation/enregistrerformationpersonnellist | |

# **deletePoste**
> FormationPersonnelDTO deletePoste(idRequest)


### Example

```typescript
import {
    FormationPersonnelControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new FormationPersonnelControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.deletePoste(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**FormationPersonnelDTO**

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

# **findFormationPersonnel**
> FormationPersonnelDTO findFormationPersonnel(idRequest)


### Example

```typescript
import {
    FormationPersonnelControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new FormationPersonnelControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.findFormationPersonnel(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**FormationPersonnelDTO**

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

# **findPointagesByPersonnelsAndDate**
> FormationPersonnelDTO findPointagesByPersonnelsAndDate(listPersonnelRequest)


### Example

```typescript
import {
    FormationPersonnelControllerApi,
    Configuration,
    ListPersonnelRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new FormationPersonnelControllerApi(configuration);

let listPersonnelRequest: ListPersonnelRequest; //

const { status, data } = await apiInstance.findPointagesByPersonnelsAndDate(
    listPersonnelRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **listPersonnelRequest** | **ListPersonnelRequest**|  | |


### Return type

**FormationPersonnelDTO**

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

# **findPostes1**
> FormationPersonnelDTO findPostes1()


### Example

```typescript
import {
    FormationPersonnelControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new FormationPersonnelControllerApi(configuration);

const { status, data } = await apiInstance.findPostes1();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**FormationPersonnelDTO**

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

# **getFormationPersonnelListJSON**
> FormationPersonnelDTO getFormationPersonnelListJSON(paginationRequest)


### Example

```typescript
import {
    FormationPersonnelControllerApi,
    Configuration,
    PaginationRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new FormationPersonnelControllerApi(configuration);

let paginationRequest: PaginationRequest; //

const { status, data } = await apiInstance.getFormationPersonnelListJSON(
    paginationRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **paginationRequest** | **PaginationRequest**|  | |


### Return type

**FormationPersonnelDTO**

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

# **getUserListJSON1**
> FormationPersonnelDTO getUserListJSON1(paginationIdRequest)


### Example

```typescript
import {
    FormationPersonnelControllerApi,
    Configuration,
    PaginationIdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new FormationPersonnelControllerApi(configuration);

let paginationIdRequest: PaginationIdRequest; //

const { status, data } = await apiInstance.getUserListJSON1(
    paginationIdRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **paginationIdRequest** | **PaginationIdRequest**|  | |


### Return type

**FormationPersonnelDTO**

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

# **saveFormationPeronnel**
> FormationPersonnelDTO saveFormationPeronnel(formationPersonnelRequest)


### Example

```typescript
import {
    FormationPersonnelControllerApi,
    Configuration,
    FormationPersonnelRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new FormationPersonnelControllerApi(configuration);

let formationPersonnelRequest: FormationPersonnelRequest; //

const { status, data } = await apiInstance.saveFormationPeronnel(
    formationPersonnelRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **formationPersonnelRequest** | **FormationPersonnelRequest**|  | |


### Return type

**FormationPersonnelDTO**

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

# **savePointagesByPersonnelsAndDate**
> FormationPersonnelDTO savePointagesByPersonnelsAndDate(listPersonnelRequest)


### Example

```typescript
import {
    FormationPersonnelControllerApi,
    Configuration,
    ListPersonnelRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new FormationPersonnelControllerApi(configuration);

let listPersonnelRequest: ListPersonnelRequest; //

const { status, data } = await apiInstance.savePointagesByPersonnelsAndDate(
    listPersonnelRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **listPersonnelRequest** | **ListPersonnelRequest**|  | |


### Return type

**FormationPersonnelDTO**

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

