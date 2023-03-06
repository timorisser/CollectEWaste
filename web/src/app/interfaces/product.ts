export interface Product {
    id?: number,
    description: string,
    product: {
        type: string,
        brand: string,
        model: string
    },
    location: number,
    status?: string,
    reservee?: string,
    collect_appointment?: number,
    distance?: number,
    owner?: string
}
