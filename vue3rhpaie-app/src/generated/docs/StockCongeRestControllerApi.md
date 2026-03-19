# StockCongeRestControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deleteStockConge**](#deletestockconge) | **DELETE** /api/personnel/stock-conges/supprimer/{id} | |
|[**findStockConge**](#findstockconge) | **POST** /api/personnel/stock-conges/trouver | |
|[**getStockCongeListJSON**](#getstockcongelistjson) | **GET** /api/personnel/stock-conges/paginer | |
|[**listerStockConges**](#listerstockconges) | **GET** /api/personnel/stock-conges/lister | |
|[**listerStockCongesParPersonnel**](#listerstockcongesparpersonnel) | **GET** /api/personnel/stock-conges/lister/personnel/{idPersonnel} | |
|[**saveStockConge**](#savestockconge) | **POST** /api/personnel/stock-conges/enregistrer | |

# **deleteStockConge**
> StockCongeDTO deleteStockConge()


### Example

```typescript
import {
    StockCongeRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new StockCongeRestControllerApi(configuration);

let id: number; // (default to undefined)

const { status, data } = await apiInstance.deleteStockConge(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | defaults to undefined|


### Return type

**StockCongeDTO**

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

# **findStockConge**
> StockCongeDTO findStockConge(stockCongeRequest)


### Example

```typescript
import {
    StockCongeRestControllerApi,
    Configuration,
    StockCongeRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new StockCongeRestControllerApi(configuration);

let stockCongeRequest: StockCongeRequest; //

const { status, data } = await apiInstance.findStockConge(
    stockCongeRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **stockCongeRequest** | **StockCongeRequest**|  | |


### Return type

**StockCongeDTO**

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

# **getStockCongeListJSON**
> StockCongeResponseObject getStockCongeListJSON()


### Example

```typescript
import {
    StockCongeRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new StockCongeRestControllerApi(configuration);

let limit: number; // (optional) (default to 10)
let offset: number; // (optional) (default to 0)
let search: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getStockCongeListJSON(
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

**StockCongeResponseObject**

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

# **listerStockConges**
> StockCongeDTO listerStockConges()


### Example

```typescript
import {
    StockCongeRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new StockCongeRestControllerApi(configuration);

const { status, data } = await apiInstance.listerStockConges();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**StockCongeDTO**

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

# **listerStockCongesParPersonnel**
> StockCongeDTO listerStockCongesParPersonnel()


### Example

```typescript
import {
    StockCongeRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new StockCongeRestControllerApi(configuration);

let idPersonnel: number; // (default to undefined)

const { status, data } = await apiInstance.listerStockCongesParPersonnel(
    idPersonnel
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idPersonnel** | [**number**] |  | defaults to undefined|


### Return type

**StockCongeDTO**

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

# **saveStockConge**
> StockCongeDTO saveStockConge(stockCongeRequest)


### Example

```typescript
import {
    StockCongeRestControllerApi,
    Configuration,
    StockCongeRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new StockCongeRestControllerApi(configuration);

let stockCongeRequest: StockCongeRequest; //

const { status, data } = await apiInstance.saveStockConge(
    stockCongeRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **stockCongeRequest** | **StockCongeRequest**|  | |


### Return type

**StockCongeDTO**

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

