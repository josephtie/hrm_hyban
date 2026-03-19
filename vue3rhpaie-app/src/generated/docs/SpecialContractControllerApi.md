# SpecialContractControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**byEmployee1**](#byemployee1) | **GET** /api/personnels/specifique/employee/{employeeId} | |
|[**create2**](#create2) | **POST** /api/personnels/specifique | |

# **byEmployee1**
> SpecialContractDTO byEmployee1()


### Example

```typescript
import {
    SpecialContractControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new SpecialContractControllerApi(configuration);

let employeeId: number; // (default to undefined)

const { status, data } = await apiInstance.byEmployee1(
    employeeId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **employeeId** | [**number**] |  | defaults to undefined|


### Return type

**SpecialContractDTO**

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

# **create2**
> SpecialContract create2(specialContract)


### Example

```typescript
import {
    SpecialContractControllerApi,
    Configuration,
    SpecialContract
} from './api';

const configuration = new Configuration();
const apiInstance = new SpecialContractControllerApi(configuration);

let specialContract: SpecialContract; //

const { status, data } = await apiInstance.create2(
    specialContract
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **specialContract** | **SpecialContract**|  | |


### Return type

**SpecialContract**

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

