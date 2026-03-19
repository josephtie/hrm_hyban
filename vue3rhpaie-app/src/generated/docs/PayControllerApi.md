# PayControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**getCurrentSelectBulletin**](#getcurrentselectbulletin) | **POST** /api/payroll/{payrollId} | |
|[**getCurrentYearBulletins**](#getcurrentyearbulletins) | **POST** /api/payroll/employe/{personnalId} | |

# **getCurrentSelectBulletin**
> BulletinPaieDTO getCurrentSelectBulletin()


### Example

```typescript
import {
    PayControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PayControllerApi(configuration);

let payrollId: number; // (default to undefined)

const { status, data } = await apiInstance.getCurrentSelectBulletin(
    payrollId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **payrollId** | [**number**] |  | defaults to undefined|


### Return type

**BulletinPaieDTO**

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

# **getCurrentYearBulletins**
> Array<BulletinPaie> getCurrentYearBulletins()


### Example

```typescript
import {
    PayControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PayControllerApi(configuration);

let personnalId: number; // (default to undefined)

const { status, data } = await apiInstance.getCurrentYearBulletins(
    personnalId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **personnalId** | [**number**] |  | defaults to undefined|


### Return type

**Array<BulletinPaie>**

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

