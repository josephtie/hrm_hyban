# ThemeControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deleteTheme**](#deletetheme) | **POST** /formation/supprimertheme | |
|[**findTheme**](#findtheme) | **POST** /formation/trouvertheme | |
|[**findThemes**](#findthemes) | **POST** /formation/listerthemes | |
|[**getUserListJSON**](#getuserlistjson) | **POST** /formation/paginerthemes | |
|[**saveTheme**](#savetheme) | **POST** /formation/enregistrertheme | |
|[**viewPoste**](#viewposte) | **GET** /formation/themes | |
|[**viewPoste1**](#viewposte1) | **HEAD** /formation/themes | |
|[**viewPoste2**](#viewposte2) | **POST** /formation/themes | |
|[**viewPoste3**](#viewposte3) | **PUT** /formation/themes | |
|[**viewPoste4**](#viewposte4) | **PATCH** /formation/themes | |
|[**viewPoste5**](#viewposte5) | **DELETE** /formation/themes | |
|[**viewPoste6**](#viewposte6) | **OPTIONS** /formation/themes | |

# **deleteTheme**
> ThemeDTO deleteTheme(idRequest)


### Example

```typescript
import {
    ThemeControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new ThemeControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.deleteTheme(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**ThemeDTO**

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

# **findTheme**
> ThemeDTO findTheme(idRequest)


### Example

```typescript
import {
    ThemeControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new ThemeControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.findTheme(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**ThemeDTO**

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

# **findThemes**
> ThemeDTO findThemes()


### Example

```typescript
import {
    ThemeControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ThemeControllerApi(configuration);

const { status, data } = await apiInstance.findThemes();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**ThemeDTO**

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

# **getUserListJSON**
> ThemeDTO getUserListJSON(paginationRequest)


### Example

```typescript
import {
    ThemeControllerApi,
    Configuration,
    PaginationRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new ThemeControllerApi(configuration);

let paginationRequest: PaginationRequest; //

const { status, data } = await apiInstance.getUserListJSON(
    paginationRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **paginationRequest** | **PaginationRequest**|  | |


### Return type

**ThemeDTO**

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

# **saveTheme**
> ThemeDTO saveTheme(themeRequest)


### Example

```typescript
import {
    ThemeControllerApi,
    Configuration,
    ThemeRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new ThemeControllerApi(configuration);

let themeRequest: ThemeRequest; //

const { status, data } = await apiInstance.saveTheme(
    themeRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **themeRequest** | **ThemeRequest**|  | |


### Return type

**ThemeDTO**

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

# **viewPoste**
> string viewPoste()


### Example

```typescript
import {
    ThemeControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ThemeControllerApi(configuration);

const { status, data } = await apiInstance.viewPoste();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**string**

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

# **viewPoste1**
> string viewPoste1()


### Example

```typescript
import {
    ThemeControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ThemeControllerApi(configuration);

const { status, data } = await apiInstance.viewPoste1();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**string**

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

# **viewPoste2**
> string viewPoste2()


### Example

```typescript
import {
    ThemeControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ThemeControllerApi(configuration);

const { status, data } = await apiInstance.viewPoste2();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**string**

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

# **viewPoste3**
> string viewPoste3()


### Example

```typescript
import {
    ThemeControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ThemeControllerApi(configuration);

const { status, data } = await apiInstance.viewPoste3();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**string**

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

# **viewPoste4**
> string viewPoste4()


### Example

```typescript
import {
    ThemeControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ThemeControllerApi(configuration);

const { status, data } = await apiInstance.viewPoste4();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**string**

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

# **viewPoste5**
> string viewPoste5()


### Example

```typescript
import {
    ThemeControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ThemeControllerApi(configuration);

const { status, data } = await apiInstance.viewPoste5();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**string**

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

# **viewPoste6**
> string viewPoste6()


### Example

```typescript
import {
    ThemeControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ThemeControllerApi(configuration);

const { status, data } = await apiInstance.viewPoste6();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**string**

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

