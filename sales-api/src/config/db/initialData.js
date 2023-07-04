import Order from "../../modules/schemas/Order.js";

export async function createInitialData(){
    await Order.collection.drop();
    let firstOrder = await Order.create({
        products: [
            {
                productId: 1001,
                quantity: 1
            },
            {
                productId: 1002,
                quantity: 1
            },
            {
                productId: 1003,
                quantity: 1
            }
        ],
        user: {
            id: 'asd321qwe',
            name: 'User test',
            email: 'usertest@email.com'
        },
        status: 'APPROVED',
        createdAt: new Date(),
        updatedAt: new Date()
    })

    let secondOrder = await Order.create({
        products: [
            {
                productId: 1002,
                quantity: 2
            },
            {
                productId: 1003,
                quantity: 2
            }
        ],
        user: {
            id: 'eq321s588',
            name: 'User test 2',
            email: 'usertest2@email.com'
        },
        status: 'REJECTED',
        createdAt: new Date(),
        updatedAt: new Date()
    })
}