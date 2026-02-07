export async function fetchInventoryHealth() {
  const response = await fetch('http://localhost:8080/inventory/health');
  if (!response.ok) {
    throw new Error('Failed to load inventory health');
  }
  return response.json();
}
