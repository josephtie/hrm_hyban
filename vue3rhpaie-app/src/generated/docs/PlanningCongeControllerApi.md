# PlanningCongeControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**getPlanningCongeListJSON**](#getplanningcongelistjson) | **POST** /api/personnels/listplanningcongejson | |
|[**savePlanningConge**](#saveplanningconge) | **POST** /api/personnels/enregistrerplanningconge | |
|[**selectPlanningConge**](#selectplanningconge) | **POST** /api/personnels/choisirplanningconge | |
|[**viewAccountType**](#viewaccounttype) | **GET** /api/personnels/planningconges/init | |

# **getPlanningCongeListJSON**
> PlanningCongeDTO getPlanningCongeListJSON(paginationRequest)


### Example

```typescript
import {
    PlanningCongeControllerApi,
    Configuration,
    PaginationRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PlanningCongeControllerApi(configuration);

let paginationRequest: PaginationRequest; //

const { status, data } = await apiInstance.getPlanningCongeListJSON(
    paginationRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **paginationRequest** | **PaginationRequest**|  | |


### Return type

**PlanningCongeDTO**

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

# **savePlanningConge**
> PlanningCongeDTO savePlanningConge(planningCongeRequest)


### Example

```typescript
import {
    PlanningCongeControllerApi,
    Configuration,
    PlanningCongeRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PlanningCongeControllerApi(configuration);

let planningCongeRequest: PlanningCongeRequest; //

const { status, data } = await apiInstance.savePlanningConge(
    planningCongeRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **planningCongeRequest** | **PlanningCongeRequest**|  | |


### Return type

**PlanningCongeDTO**

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

# **selectPlanningConge**
> PlanningConge selectPlanningConge(idRequest)


### Example

```typescript
import {
    PlanningCongeControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PlanningCongeControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.selectPlanningConge(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**PlanningConge**

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

# **viewAccountType**
> object viewAccountType()


### Example

```typescript
import {
    PlanningCongeControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PlanningCongeControllerApi(configuration);

const { status, data } = await apiInstance.viewAccountType();
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

