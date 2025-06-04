// fetch-interceptor.js

const originalFetch = window.fetch;

window.fetch = function(resource, init) {
  if (typeof resource === 'string') {
    resource = resource.replace('localhost', 'pennymate.3utilities.com');
  } else if (resource instanceof Request) {
    const newUrl = resource.url.replace('localhost', 'pennymate.3utilities.com');
    resource = new Request(newUrl, resource);
  }
  return originalFetch(resource, init);
};
