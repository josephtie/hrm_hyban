# TypeContratControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**listeTypeContrat**](#listetypecontrat) | **POST** /parametrages/listetypecontrat | |

# **listeTypeContrat**
> Array<TypeContrat> listeTypeContrat()


### Example

```typescript
import {
    TypeContratControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TypeContratControllerApi(configuration);

const { status, data } = await apiInstance.listeTypeContrat();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<TypeContrat>**

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

